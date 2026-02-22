package ordermanager.domain.service;

import ordermanager.domain.model.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderPricingService {


    private static final BigDecimal DELIVERY_FEE_PCT = new BigDecimal("0.05");
    private static final BigDecimal MIN_DELIVERY_FEE = new BigDecimal("2.00");
    private static final BigDecimal TAX_PCT = new BigDecimal("0.10");
    private static final int MONEY_SCALE = 2;//lo nishan krdini point y Currency.
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;


    public void ApplyPricing(Order order) {

        BigDecimal subtotal = order.getItems().stream()
                .map(this::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setSubTotal(subtotal);

        BigDecimal deliveryFee = subtotal.multiply(DELIVERY_FEE_PCT)
                .max(MIN_DELIVERY_FEE)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setDeliveryFee(deliveryFee);

        BigDecimal tax = subtotal.multiply(TAX_PCT)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setTax(tax);

        BigDecimal total = subtotal.add(deliveryFee).add(tax)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setTotalPrice(total);
    }
    private BigDecimal lineTotal(OrderItem oi) {
        Item item = oi.getItem();
        BigDecimal price = safe(item.getPrice());
        BigDecimal discount = roundDiscountWithinRange(safe(item.getDiscount()));
        BigDecimal qty = BigDecimal.valueOf(oi.getQuantity());
        BigDecimal unitNet = price.multiply(BigDecimal.ONE.subtract(discount));
        return unitNet.multiply(qty);
    }


    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private BigDecimal roundDiscountWithinRange(BigDecimal v) {
        if (v.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;
        if (v.compareTo(BigDecimal.ONE) > 0) return BigDecimal.ONE;
        return v;
    }

}

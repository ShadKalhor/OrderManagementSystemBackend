package ordermanager.domain.service;

import ordermanager.infrastructure.web.dto.item.ItemDto;
import ordermanager.infrastructure.web.dto.order.OrderDto;
import ordermanager.infrastructure.web.dto.orderitem.OrderItemDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderPricingService {


    private static final BigDecimal DELIVERY_FEE_PCT = new BigDecimal("0.05");
    private static final BigDecimal MIN_DELIVERY_FEE = new BigDecimal("2.00");
    private static final BigDecimal TAX_PCT = new BigDecimal("0.10");
    private static final int MONEY_SCALE = 2;//lo nishan krdini point y Currency.
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;


    public OrderDto ApplyPricing(OrderDto orderDto) {

        BigDecimal subtotal = calculateSubtotal(orderDto.items());
        BigDecimal deliveryFee = calculateDeliveryFee(subtotal);
        BigDecimal tax = calculateTax(subtotal);
        BigDecimal total = calculateTotal(subtotal,deliveryFee,tax);

        return new OrderDto(orderDto.items(), subtotal, deliveryFee,tax,total);
    }

    private BigDecimal calculateSubtotal(List<OrderItemDto> orderItemDtos){
         return orderItemDtos.stream()
                .map(this::lineTotal)
                 .map(OrderItemDto::totalPrice)
                 .reduce(BigDecimal.ZERO, BigDecimal::add)
                 .setScale(MONEY_SCALE, ROUNDING);

    }

    private BigDecimal calculateDeliveryFee(BigDecimal subtotal){
        return subtotal.multiply(DELIVERY_FEE_PCT)
                .max(MIN_DELIVERY_FEE)
                .setScale(MONEY_SCALE, ROUNDING);
    }

    private BigDecimal calculateTax(BigDecimal subtotal){
        return subtotal.multiply(TAX_PCT)
                .setScale(MONEY_SCALE, ROUNDING);
    }

    private BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal deliveryFee, BigDecimal tax){
        return subtotal.add(deliveryFee).add(tax)
                .setScale(MONEY_SCALE, ROUNDING);
    }

    private OrderItemDto lineTotal(OrderItemDto oi) {
        ItemDto item = oi.item();
        BigDecimal price = safe(item.price());
        BigDecimal discount = roundDiscountWithinRange(safe(item.discount()));
        BigDecimal qty = BigDecimal.valueOf(oi.quantity());
        BigDecimal unitNet = price.multiply(BigDecimal.ONE.subtract(discount));
        return new OrderItemDto(oi.item(), oi.quantity(), unitNet.multiply(qty));
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

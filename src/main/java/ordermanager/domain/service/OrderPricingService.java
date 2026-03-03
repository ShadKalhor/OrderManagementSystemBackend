package ordermanager.domain.service;

import ordermanager.domain.model.ItemDomain;
import ordermanager.domain.model.OrderDomain;
import ordermanager.domain.model.OrderItemDomain;
import ordermanager.domain.port.out.ItemPersistencePort;
import ordermanager.domain.port.out.OrderItemPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.OrderItem;


import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;
import java.util.UUID;

public class OrderPricingService {

    private static final BigDecimal DELIVERY_FEE_PCT = new BigDecimal("0.05");
    private static final BigDecimal MIN_DELIVERY_FEE = new BigDecimal("2.00");
    private static final BigDecimal TAX_PCT = new BigDecimal("0.10");
    private static final int MONEY_SCALE = 2;//lo nishan krdini point y Currency.
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private OrderItemPersistencePort orderItemPort;
    private ItemPersistencePort itemPort;

    public OrderDomain ApplyPricing(OrderDomain orderDomain) {

        BigDecimal subtotal = calculateSubtotal(orderDomain.getItemIds());
        BigDecimal deliveryFee = calculateDeliveryFee(subtotal);
        BigDecimal tax = calculateTax(subtotal);
        BigDecimal total = calculateTotal(subtotal,deliveryFee,tax);

        return new OrderDomain(orderDomain.getItemIds(), subtotal, deliveryFee,tax,total);
    }

    private BigDecimal calculateSubtotal(List<UUID> orderItemIds){

        List<OrderItemDomain> orderItemDomains = orderItemPort.GetOrderItemsByIds(orderItemIds);
         return orderItemDomains.stream()
                .map(this::lineTotal)
                 .map(OrderItemDomain::getTotalPrice)
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

    private OrderItemDomain lineTotal(OrderItemDomain oi) {
        ItemDomain item = itemPort.findById(oi.getItemId()).get();
        BigDecimal price = safe(item.getPrice());
        BigDecimal discount = roundDiscountWithinRange(safe(item.getDiscount()));
        BigDecimal qty = BigDecimal.valueOf(oi.getQuantity());
        BigDecimal unitNet = price.multiply(BigDecimal.ONE.subtract(discount));
        return new OrderItemDomain(UUID.randomUUID(), oi.getItemId(), oi.getQuantity(), unitNet.multiply(qty));
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

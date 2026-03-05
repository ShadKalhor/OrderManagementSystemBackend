package ordermanager.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class OrderDomain {

    private UUID id;
    private UUID userId;
    private UUID addressId;
    private UUID driverId;
    private List<UUID> orderItemIds;
    private UUID reservationId;

    private Status status;
    private DeliveryStatus deliveryStatus;

    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private String notes;


    public OrderDomain(List<UUID> orderItemIds, BigDecimal subtotal,
                       BigDecimal deliveryFee, BigDecimal tax, BigDecimal total) {
        this.orderItemIds = orderItemIds;
        this.subTotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.tax = tax;
        this.totalPrice = total;

    }
}

package ordermanager.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class Order {

    private UUID id;
    private User user;
    private UserAddress address;
    private Driver driver;
    private Utilities.Status status;
    private Utilities.DeliveryStatus deliveryStatus;
    private List<OrderItem> items;
    private Reservation reservation;
    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private String notes;



    public void addItem(OrderItem item) {
        if(items == null)
            items = new ArrayList<>();
        if(item.getId() == null)
            item.setId(UUID.randomUUID());
        this.items.add(item);
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        if (this.items != null) {
            for (var oi : this.items) {
                oi.setOrder(this);
                if (oi.getId() == null) oi.setId(UUID.randomUUID());
            }
        }
    }


}

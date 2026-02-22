package ordermanager.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class OrderItem {

    private UUID id;

    private Item item;
    private int quantity;
    private BigDecimal totalPrice;
    private Order order;



    public OrderItem(Item item, int quantity, BigDecimal totalPrice) {
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    public OrderItem(UUID id, Item item, int quantity, BigDecimal totalPrice) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

}

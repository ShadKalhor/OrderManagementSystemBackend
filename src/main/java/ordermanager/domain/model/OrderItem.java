package ordermanager.domain.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name = "OrderItem")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne()
    @JoinColumn(name = "itemId")
    private Item item;
    private int quantity;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "orderId")
    @Type(type = "uuid-char")
    private Order order;

    public OrderItem(Item item, int quantity, BigDecimal totalPrice) {
        this.id = UUID.randomUUID();
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    public OrderItem(UUID id,Item item, int quantity, BigDecimal totalPrice) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}

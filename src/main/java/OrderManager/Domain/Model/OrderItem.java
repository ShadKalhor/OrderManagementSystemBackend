package OrderManager.Entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;
@Entity
@Table(name = "OrderItem")
public class OrderItem {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne()
    @JoinColumn(name = "itemId")
    private Item item;
    private int quantity;
    private double totalPrice;
    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    @Type(type = "uuid-char")
    private Order order;

    public OrderItem() {}

    public OrderItem(Item item, int quantity, double totalPrice) {
        this.id = UUID.randomUUID();
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    public OrderItem(UUID id,Item item, int quantity, double totalPrice) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

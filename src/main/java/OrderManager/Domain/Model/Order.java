package OrderManager.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @OneToOne
    @JoinColumn(name = "addressId")
    private UserAddress address;

    @ManyToOne
    @JoinColumn(name = "driverId")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private Utilities.Status status;

    @Enumerated(EnumType.STRING)
    private Utilities.DeliveryStatus deliveryStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    private double subTotal;
    private double deliveryFee;
    private double tax;
    private double totalPrice;
    private String notes;
    public void addItem(OrderItem item) {
        if(items == null)
            items = new ArrayList<>();
        this.items.add(item);
    }
}

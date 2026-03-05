package ordermanager.infrastructure.store.persistence.entity;

import lombok.*;
import ordermanager.domain.model.DeliveryStatus;
import ordermanager.domain.model.Status;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "Orders")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
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
    private Status status;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToOne()
    @JoinColumn(name = "reservationId")
    private Reservation reservation;


    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private String notes;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }

    public void addItem(OrderItem item) {
        if(orderItems == null)
            orderItems = new ArrayList<>();
        if(item.getId() == null)
            item.setId(UUID.randomUUID());
        this.orderItems.add(item);
    }

    public void setItems(List<OrderItem> items) {
        this.orderItems = items;
        if (this.orderItems != null) {
            for (var oi : this.orderItems) {
                oi.setOrder(this);
                if (oi.getId() == null) oi.setId(UUID.randomUUID());
            }
        }
    }


}


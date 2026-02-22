package ordermanager.infrastructure.store.persistence.entity;

import lombok.*;
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
    private List<OrderItem> items;

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


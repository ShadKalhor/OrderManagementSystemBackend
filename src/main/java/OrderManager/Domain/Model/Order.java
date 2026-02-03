package OrderManager.Domain.Model;

import OrderManager.Shared.Validation.EnumOrNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
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

    @OneToOne()
    @JoinColumn(name = "reservationId")
    private Reservation reservation;


    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private String notes;
    public void addItem(OrderItem item) {
        if(items == null)
            items = new ArrayList<>();
        this.items.add(item);
    }
}


/*

UUID userId,
UUID addressId,
UUID driverId,*/
/*
        @Valid @NotEmpty List<CreateOrderItemRequest> items,*//*

@EnumOrNull(Utilities.Status.class)
Utilities.Status status,
@EnumOrNull(Utilities.DeliveryStatus.class)
Utilities.DeliveryStatus deliveryStatus,
@Size(max=255) String notes
*/

package ordermanager.domain.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(
        name = "reservation_lines",
        uniqueConstraints = @UniqueConstraint(name = "uq_reservation_line_res_item", columnNames = {"reservation_id", "item_id"})
)
@Getter @Setter @NoArgsConstructor
public class ReservationLine {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(nullable = false)
    private int qty;

    public ReservationLine(Reservation reservation, UUID itemId, int qty) {
        this.reservation = reservation;
        this.itemId = itemId;
        this.qty = qty;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }

}



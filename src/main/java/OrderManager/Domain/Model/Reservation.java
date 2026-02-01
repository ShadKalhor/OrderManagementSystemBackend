package OrderManager.Domain.Model;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;
import java.util.*;
import java.util.UUID;
import OrderManager.Domain.Model.Utilities.ReservationStatus;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "reservations")
@Getter @Setter @NoArgsConstructor
public class Reservation {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Instant expiresAt;
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationLine> lines = new ArrayList<>();

}




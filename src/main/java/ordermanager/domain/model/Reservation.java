package ordermanager.domain.model;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;
import java.util.*;
import java.util.UUID;
import ordermanager.domain.model.Utilities.ReservationStatus;
import org.hibernate.annotations.Type;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "reservations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Reservation {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Instant expiresAt;
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationLine> lines = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }

}




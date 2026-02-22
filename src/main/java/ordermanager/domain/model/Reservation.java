package ordermanager.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class Reservation {

    private UUID id;
    private Utilities.ReservationStatus status;
    private Instant expiresAt;
    private Instant createdAt = Instant.now();
    private List<ReservationLine> lines = new ArrayList<>();




}

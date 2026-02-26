package ordermanager.domain.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
public class ReservationDomain {

    private UUID id;
    private ReservationStatus status;

    private Instant expiresAt;
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private List<UUID> linesIds = new ArrayList<>();


}

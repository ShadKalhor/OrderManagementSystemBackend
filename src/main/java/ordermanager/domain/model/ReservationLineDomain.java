package ordermanager.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Setter
@Getter
@Builder
public class ReservationLineDomain {

    private UUID id;
    private UUID itemId;
    private int qty;

}

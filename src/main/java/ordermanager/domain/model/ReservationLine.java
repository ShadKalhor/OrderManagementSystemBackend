package ordermanager.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class ReservationLine {

    private UUID id;
    private Reservation reservation;
    private UUID itemId;
    private int qty;

    public ReservationLine(Reservation reservation, UUID itemId, int qty) {
        this.reservation = reservation;
        this.itemId = itemId;
        this.qty = qty;
    }

}

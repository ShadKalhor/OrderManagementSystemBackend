package ordermanager.application.port.out;

import ordermanager.domain.model.OrderItem;
import ordermanager.domain.model.Reservation;

import java.util.List;
import java.util.UUID;

public interface InventoryReservationPort {
    ReservationResult reserveItems(List<OrderItem> items);
    void releaseReservation(UUID reservationId);
    void confirmReservation(UUID reservationId);
    Reservation FindReservationById(UUID reservationId);
}

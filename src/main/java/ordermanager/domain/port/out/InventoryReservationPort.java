package ordermanager.domain.port.out;

import ordermanager.infrastructure.store.persistence.entity.OrderItem;
import ordermanager.infrastructure.store.persistence.entity.Reservation;

import java.util.List;
import java.util.UUID;

public interface InventoryReservationPort {
    ReservationResult reserveItems(List<OrderItem> items);
    void releaseReservation(UUID reservationId);
    void confirmReservation(UUID reservationId);
    Reservation FindReservationById(UUID reservationId);
}

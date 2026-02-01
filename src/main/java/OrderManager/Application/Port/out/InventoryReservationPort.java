package OrderManager.Application.Port.out;

import OrderManager.Domain.Model.OrderItem;
import OrderManager.Domain.Model.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryReservationPort {
    ReservationResult reserveItems(List<OrderItem> items);
    void releaseReservation(UUID reservationId);
    void confirmReservation(UUID reservationId);
    Reservation FindReservationById(UUID reservationId);
}

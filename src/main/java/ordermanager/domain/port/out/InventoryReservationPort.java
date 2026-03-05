package ordermanager.domain.port.out;

import io.vavr.control.Either;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.OrderItemDomain;
import ordermanager.domain.model.ReservationDomain;
import ordermanager.domain.model.ReservationResult;

import java.util.List;
import java.util.UUID;

public interface InventoryReservationPort {
    ReservationResult reserveItems(List<OrderItemDomain> items);
    void releaseReservation(UUID reservationId);
    void confirmReservation(UUID reservationId);
    Either<StructuredError, ReservationDomain> FindReservationById(UUID reservationId);
}

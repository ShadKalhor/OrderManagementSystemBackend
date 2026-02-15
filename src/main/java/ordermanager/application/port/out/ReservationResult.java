package ordermanager.application.port.out;

import java.util.List;
import java.util.UUID;


public sealed interface ReservationResult
        permits ReservationResult.Success, ReservationResult.Failure {
    record Success(UUID reservationId) implements ReservationResult {}
    record Failure(List<UnavailableLine> lines) implements ReservationResult {}
    record UnavailableLine(UUID itemId, int requested, int available) {}
}

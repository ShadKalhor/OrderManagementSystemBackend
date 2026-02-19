package ordermanager.infrastructure.exception;

import ordermanager.domain.port.out.ReservationResult;

import java.util.List;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(List<ReservationResult.UnavailableLine> lines) {
        super("Insufficient inventory: " + lines);
    }
}

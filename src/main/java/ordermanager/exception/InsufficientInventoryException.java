package ordermanager.exception;

import ordermanager.application.port.out.ReservationResult;

import java.util.List;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(List<ReservationResult.UnavailableLine> lines) {
        super("Insufficient inventory: " + lines);
    }
}

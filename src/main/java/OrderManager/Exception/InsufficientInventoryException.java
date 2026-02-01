package OrderManager.Exception;

import OrderManager.Application.Port.out.ReservationResult;

import java.util.List;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(List<ReservationResult.UnavailableLine> lines) {
        super("Insufficient inventory: " + lines);
    }
}

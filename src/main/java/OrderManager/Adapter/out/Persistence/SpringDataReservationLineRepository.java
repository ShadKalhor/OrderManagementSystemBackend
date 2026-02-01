package OrderManager.Adapter.out.Persistence;

import OrderManager.Domain.Model.ReservationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataReservationLineRepository extends JpaRepository<ReservationLine, UUID> {}

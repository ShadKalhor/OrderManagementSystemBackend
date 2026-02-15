package ordermanager.adapter.out.persistence;

import ordermanager.domain.model.ReservationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataReservationLineRepository extends JpaRepository<ReservationLine, UUID> {}

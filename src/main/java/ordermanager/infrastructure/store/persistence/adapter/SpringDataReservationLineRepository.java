package ordermanager.infrastructure.store.persistence.adapter;

import ordermanager.infrastructure.store.persistence.entity.ReservationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataReservationLineRepository extends JpaRepository<ReservationLine, UUID> {}

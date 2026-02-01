package OrderManager.Adapter.out.Persistence;

import java.util.UUID;

import OrderManager.Domain.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReservationRepository extends JpaRepository<Reservation, UUID> {}

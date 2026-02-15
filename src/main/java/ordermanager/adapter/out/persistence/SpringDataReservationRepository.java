package ordermanager.adapter.out.persistence;

import java.util.UUID;

import ordermanager.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReservationRepository extends JpaRepository<Reservation, UUID> {}

package ordermanager.infrastructure.store.persistence.jpa;

import java.util.UUID;

import ordermanager.infrastructure.store.persistence.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReservationRepository extends JpaRepository<Reservation, UUID> {}

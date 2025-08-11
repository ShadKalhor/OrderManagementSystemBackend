package OrderManager.Adapter.out.Persistence;

import OrderManager.Domain.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataOrderRepository extends JpaRepository<Order, UUID> {

    Optional<List<Order>> findByUserId(UUID userId);
}

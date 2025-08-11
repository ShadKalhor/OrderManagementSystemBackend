package OrderManager.Adapter.out.Persistence;

import OrderManager.Domain.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataOrderItemRepository extends JpaRepository<OrderItem, UUID> {

}

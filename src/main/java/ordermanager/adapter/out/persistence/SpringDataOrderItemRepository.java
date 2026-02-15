package ordermanager.adapter.out.persistence;

import ordermanager.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataOrderItemRepository extends JpaRepository<OrderItem, UUID> {

}

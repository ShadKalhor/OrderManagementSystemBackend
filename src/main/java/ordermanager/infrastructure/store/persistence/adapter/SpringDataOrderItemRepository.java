package ordermanager.infrastructure.store.persistence.adapter;

import ordermanager.infrastructure.store.persistence.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataOrderItemRepository extends JpaRepository<OrderItem, UUID> {

}

package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataOrderRepository extends JpaRepository<Order, UUID> {

    Option<List<Order>> findByUserId(UUID userId);
    Option<Order> findOptionById(UUID uuid);
}

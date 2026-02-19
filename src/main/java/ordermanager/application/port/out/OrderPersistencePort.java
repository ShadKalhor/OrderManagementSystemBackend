package ordermanager.application.port.out;

import ordermanager.infrastructure.store.persistence.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderPersistencePort {
    Order save(Order order);

    List<Order> findAll();

    Optional<Order> findById(UUID orderId);

    void deleteById(UUID orderId);

    Optional<List<Order>> findByUserId(UUID userId);
}

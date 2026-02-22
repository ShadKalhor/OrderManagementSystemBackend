package ordermanager.domain.port.out;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderPersistencePort {
    Option<Order> save(Order order);

    List<Order> findAll();

    Option<Order> findById(UUID orderId);

    void deleteById(UUID orderId);

    Option<List<Order>> findByUserId(UUID userId);
}

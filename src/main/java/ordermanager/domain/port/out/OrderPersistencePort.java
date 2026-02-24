package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.infrastructure.store.persistence.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderPersistencePort {
    Either<StructuredError, Order> save(Order order);

    List<Order> findAll();

    Option<Order> findById(UUID orderId);

    Either<StructuredError, Void> deleteById(UUID orderId);

    List<Order> findByUserId(UUID userId);
}

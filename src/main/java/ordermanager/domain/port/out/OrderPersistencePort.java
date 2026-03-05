package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.OrderDomain;


import java.util.List;
import java.util.UUID;

public interface OrderPersistencePort {
    Either<StructuredError, OrderDomain> save(OrderDomain order);

    List<OrderDomain> findAll();

    Option<OrderDomain> findById(UUID orderId);

    Either<StructuredError, Void> deleteById(UUID orderId);

    List<OrderDomain> findByUserId(UUID userId);
}

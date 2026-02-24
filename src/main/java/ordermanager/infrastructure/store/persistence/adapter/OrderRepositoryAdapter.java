package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.port.out.OrderPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.Order;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepositoryAdapter implements OrderPersistencePort {

    SpringDataOrderRepository orderRepository;

    public OrderRepositoryAdapter(SpringDataOrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public Either<StructuredError, Order> save(Order order) {

        return Try.of(() -> orderRepository.save(order)).toEither(new StructuredError("Could Not Save Order", ErrorType.SERVER_ERROR));
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Option<Order> findById(UUID orderId) {
        return orderRepository.findOptionById(orderId);
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID orderId) {
        return Try.run(() -> orderRepository.deleteById(orderId)).toEither(new StructuredError("Could Not Delete Order",ErrorType.SERVER_ERROR));
    }

    @Override
    public List<Order> findByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }
}

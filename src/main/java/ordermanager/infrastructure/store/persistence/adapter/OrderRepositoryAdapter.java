package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.OrderDomain;
import ordermanager.domain.port.out.OrderPersistencePort;
import ordermanager.infrastructure.mapper.OrderMapper;
import ordermanager.infrastructure.store.persistence.entity.Order;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepositoryAdapter implements OrderPersistencePort {

    SpringDataOrderRepository orderRepository;
    private OrderMapper orderMapper;


    public OrderRepositoryAdapter(SpringDataOrderRepository orderRepository, OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Either<StructuredError, OrderDomain> save(OrderDomain domain) {
        Order order = orderMapper.toEntity(domain);
        return Try.of(() -> orderRepository.save(order)).map(orderMapper::toDomain).toEither(new StructuredError("Could Not Save Order", ErrorType.SERVER_ERROR));
    }

    @Override
    public List<OrderDomain> findAll() {
        return orderRepository.findAll().stream().map(orderMapper::toDomain).toList();
    }

    @Override
    public Option<OrderDomain> findById(UUID orderId) {
        return orderRepository.findOptionById(orderId).map(orderMapper::toDomain);
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID orderId) {
        return Try.run(() -> orderRepository.deleteById(orderId)).toEither(new StructuredError("Could Not Delete Order",ErrorType.SERVER_ERROR));
    }

    @Override
    public List<OrderDomain> findByUserId(UUID userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toDomain).toList();
    }
}

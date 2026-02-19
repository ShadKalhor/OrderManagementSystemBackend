package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.domain.port.out.OrderPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryAdapter implements OrderPersistencePort {

    SpringDataOrderRepository orderRepository;

    public OrderRepositoryAdapter(SpringDataOrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public Option<Order> save(Order order) {
        return Option.of(orderRepository.save(order));
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
    public void deleteById(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Option<List<Order>> findByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }
}

package OrderManager.Adapter.out.Persistence;

import OrderManager.Application.Port.out.OrderPersistencePort;
import OrderManager.Domain.Model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderRepositoryAdapter implements OrderPersistencePort {

    SpringDataOrderRepository orderRepository;

    public OrderRepositoryAdapter(SpringDataOrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public void deleteById(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Optional<List<Order>> findByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }
}

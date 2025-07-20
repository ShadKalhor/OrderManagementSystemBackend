package OrderManager.Service;

import OrderManager.Entities.Item;
import OrderManager.Entities.Order;
import OrderManager.Entities.OrderItem;
import OrderManager.Entities.Utilities;
import OrderManager.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Optional<Order> CreateOrder(Order order){
        Map<String,String> unavailableItems = itemService.GetUnavailableItems(order.getItems());
        if (unavailableItems !=null){
            System.out.println("There is these unavailable items\n" + unavailableItems);
            return Optional.empty();
        }
        if (order.getStatus() != Utilities.Status.Pending)
            order = DeductAmountFromInventory(order);


        order = CalculateOrder(order);
        Optional<Order> result = Optional.of(orderRepository.save(order));
        return result;
    }

    public Optional<List<Order>> GetAllOrders(){
        return Optional.of( orderRepository.findAll());
    }


    public Optional<Order> GetOrderById(UUID orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        return order;
    }

    public boolean DeleteOrder(UUID orderId){
        Optional<Order> order = GetOrderById(orderId);
        if(order.isPresent()) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }

    public Optional<List<Order>> GetByUserId(UUID userId) {
        Optional<List<Order>> orders = orderRepository.findByUserId(userId);
        return orders;
    }


    private Order CalculateOrder(Order order){
        double subtotal = calculateSubtotal(order.getItems());
        order.setSubTotal(subtotal);
        order.setDeliveryFee(
                Math.max(subtotal*0.05, 2.0)
        );
        order.setTax(
                subtotal*0.1
        );
        order.setTotalPrice(
                order.getSubTotal() + order.getDeliveryFee() + order.getTax()
        );

        return order;
    }

    private double calculateSubtotal(List<OrderItem> orderItems){
        double subtotal = 0.0;
        for (OrderItem orderItem : orderItems){
            Optional<Item> itemOpt = itemService.GetItemById(orderItem.getItem().getId());

            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                orderItem.setItem(item);// <-- Now this works
            } else {
                throw new EntityNotFoundException("Item not found with ID: " + orderItem.getItem().getId());
            }
            orderItem.setTotalPrice(
                    orderItem.getQuantity() *
                            (orderItem.getItem().getPrice() -
                                    (orderItem.getItem().getPrice() * orderItem.getItem().getDiscount()))
            );
            subtotal+=orderItem.getTotalPrice();
        }
        return subtotal;
    }


    private Order DeductAmountFromInventory(Order order){
        for (OrderItem orderItem : order.getItems()){
            orderItem.getItem().setQuantity(
                    orderItem.getItem().getQuantity() - orderItem.getQuantity()
            );
        }
        return order;
    }
}



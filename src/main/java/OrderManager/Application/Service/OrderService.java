package OrderManager.Application.Service;

import OrderManager.Application.Port.out.OrderPersistencePort;
import OrderManager.Domain.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final ItemService itemService;

    private final OrderPersistencePort orderPort;

    public OrderService(OrderPersistencePort orderPort, ItemService itemService){
        this.itemService = itemService;
        this.orderPort = orderPort;
    }
    
    
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
        Optional<Order> result = Optional.of(orderPort.save(order));
        return result;
    }

    public List<Order> GetAllOrders(){
        return orderPort.findAll();
    }


    public Optional<Order> GetOrderById(UUID orderId){
        Optional<Order> order = orderPort.findById(orderId);
        return order;
    }

    public boolean DeleteOrder(UUID orderId){
        return orderPort.findById(orderId)
                .map(d -> {
                    orderPort.deleteById(orderId);
                    return true;
                })
                .orElse(false);

    }

    public Optional<List<Order>> GetByUserId(UUID userId) {
        Optional<List<Order>> orders = orderPort.findByUserId(userId);
        return orders;
    }


    private Order CalculateOrder(Order order){
        BigDecimal subtotal = calculateSubtotal(order.getItems());
        BigDecimal minFee = new BigDecimal("2.00");
        BigDecimal feePct = new BigDecimal("0.05");

        BigDecimal taxPct = new BigDecimal("0.10");

        order.setSubTotal(subtotal);


        BigDecimal deliveryFee = subtotal.multiply(feePct);
        if (deliveryFee.compareTo(minFee) < 0) {
            deliveryFee = minFee;
        }
        order.setDeliveryFee(deliveryFee);

        BigDecimal tax = subtotal.multiply(taxPct);
        order.setTax(tax);

        BigDecimal total = subtotal.add(deliveryFee).add(tax);
        order.setTotalPrice(total);

        return order;
    }

    private BigDecimal calculateSubtotal(List<OrderItem> orderItems){

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItems) {
            Optional<Item> itemOpt = itemService.GetItemById(orderItem.getItem().getId());
            Item item = itemOpt.orElseThrow(() ->
                    new EntityNotFoundException("Item not found with ID: " + orderItem.getItem().getId())
            );
            orderItem.setItem(item); // hydrate

            BigDecimal price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
            BigDecimal discount = item.getDiscount() != null ? item.getDiscount() : BigDecimal.ZERO;
            BigDecimal qty = BigDecimal.valueOf(orderItem.getQuantity());

            BigDecimal oneMinusDiscount = BigDecimal.ONE.subtract(discount);
            if (oneMinusDiscount.compareTo(BigDecimal.ZERO) < 0) {
                oneMinusDiscount = BigDecimal.ZERO; // guard if discount > 1
            }

            BigDecimal unitNet = price.multiply(oneMinusDiscount);
            BigDecimal lineTotal = unitNet.multiply(qty);

            orderItem.setTotalPrice(lineTotal);
            subtotal = subtotal.add(lineTotal);
        }

        return subtotal;

        /* BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems){
            Optional<Item> itemOpt = itemService.GetItemById(orderItem.getItem().getId());

            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                orderItem.setItem(item);
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
*/
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



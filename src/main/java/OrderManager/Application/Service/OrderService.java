package OrderManager.Application.Service;

import OrderManager.Application.Port.out.InventoryReservationPort;
import OrderManager.Application.Port.out.OrderPersistencePort;
import OrderManager.Application.Port.out.ReservationResult;
import OrderManager.Domain.Model.*;
import OrderManager.Exception.EntityNotFoundException;
import OrderManager.Exception.InsufficientInventoryException;
import OrderManager.Exception.ValidationException;
import OrderManager.Shared.Dto.OrderDtos.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionScoped;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private static final BigDecimal DELIVERY_FEE_PCT = new BigDecimal("0.05");
    private static final BigDecimal MIN_DELIVERY_FEE = new BigDecimal("2.00");
    private static final BigDecimal TAX_PCT = new BigDecimal("0.10");
    private static final int MONEY_SCALE = 2;//lo nishan krdini point y Currency.
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;




    private final ItemService itemService;


    private final OrderPersistencePort orderPort;

    private final InventoryReservationPort inventoryReservationPort;

    public OrderService(OrderPersistencePort orderPort, ItemService itemService, InventoryReservationPort inventoryReservationPort){
        this.itemService = itemService;
        this.orderPort = orderPort;
        this.inventoryReservationPort = inventoryReservationPort;
    }

    @Transactional
    public Optional<Order> CreateOrder(Order draft) {
        hydrateItems(draft);
        validateDraft(draft);

        //reserve y item akan daka gar hatu item habu unavailable bu result dabta instance ak la ReservationResult.Failure
        var result = inventoryReservationPort.reserveItems(draft.getItems());
        if (result instanceof ReservationResult.Failure f) {
            throw new InsufficientInventoryException(f.lines());
        }
        var reservationId = ((ReservationResult.Success) result).reservationId();

        try {

            applyPricing(draft);


            draft.setReservation(inventoryReservationPort.FindReservationById(reservationId));
            draft.setStatus(Utilities.Status.Pending);
            return Optional.of(orderPort.save(draft));
        } catch (RuntimeException ex) {

            inventoryReservationPort.releaseReservation(reservationId);
            throw ex;
        }
    }


    //dastkari item&price detail nakre.
    @Transactional
    public Order UpdateOrder(UUID orderId, Order patchedOrder){

        Optional<Order> orderExists = orderPort.findById(orderId);
        if(orderExists.isEmpty())
            throw new EntityNotFoundException("Order Not Found with Id ", orderId);
        Order order = orderExists.get();

        order.setAddress(patchedOrder.getAddress());
        order.setDriver(patchedOrder.getDriver());
        order.setStatus(patchedOrder.getStatus());
        order.setDeliveryStatus(patchedOrder.getDeliveryStatus());
        order.setNotes(patchedOrder.getNotes());
        return orderPort.save(order);

        /*
        Optional<Order> orderExists = GetOrderById(orderId);
        Order currentOrderInfo;
        if(orderExists.isEmpty())
            throw new EntityNotFoundException("Order", orderId);
        else
            currentOrderInfo = orderExists.get();


        order.setDeliveryFee(currentOrderInfo.getDeliveryFee());
        order.setReservation(currentOrderInfo.getReservation());
        order.setSubTotal(currentOrderInfo.getSubTotal());
        order.setTax(currentOrderInfo.getTax());
        order.setTotalPrice(currentOrderInfo.getTotalPrice());
        order.setItems(currentOrderInfo.getItems());

        order.setId(orderId);
        orderPort.save(order);
*/
    }


    public List<Order> GetAllOrders(){
        return orderPort.findAll();
    }


    public Optional<Order> GetOrderById(UUID orderId){
        Optional<Order> order = orderPort.findById(orderId);
        return order;
    }


    public Optional<List<Order>> GetByUserId(UUID userId) {
        Optional<List<Order>> orders = orderPort.findByUserId(userId);
        return orders;
    }


    public boolean DeleteOrder(UUID orderId){
        return orderPort.findById(orderId)
                .map(d -> {
                    orderPort.deleteById(orderId);
                    return true;
                })
                .orElse(false);

    }

    private void applyPricing(Order order) {

        BigDecimal subtotal = order.getItems().stream()
                .map(this::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setSubTotal(subtotal);

        BigDecimal deliveryFee = subtotal.multiply(DELIVERY_FEE_PCT)
                .max(MIN_DELIVERY_FEE)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setDeliveryFee(deliveryFee);

        BigDecimal tax = subtotal.multiply(TAX_PCT)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setTax(tax);

        BigDecimal total = subtotal.add(deliveryFee).add(tax)
                .setScale(MONEY_SCALE, ROUNDING);
        order.setTotalPrice(total);
    }
    private BigDecimal lineTotal(OrderItem oi) {
        Item item = oi.getItem();
        BigDecimal price = safe(item.getPrice());
        BigDecimal discount = roundDiscountWithinRange(safe(item.getDiscount()));
        BigDecimal qty = BigDecimal.valueOf(oi.getQuantity());
        BigDecimal unitNet = price.multiply(BigDecimal.ONE.subtract(discount));
        return unitNet.multiply(qty);
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private BigDecimal roundDiscountWithinRange(BigDecimal v) {
        if (v.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;
        if (v.compareTo(BigDecimal.ONE) > 0) return BigDecimal.ONE;
        return v;
    }


    private void hydrateItems(Order order) {
        for (OrderItem oi : order.getItems()) {
            UUID itemId = oi.getItem().getId();

            Item item = itemService.GetItemById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Item", itemId));

            oi.setItem(item);
        }
    }


    private void validateDraft(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new ValidationException("Order must contain at least one item");
        }

        for (OrderItem oi : order.getItems()) {
            if (oi.getQuantity() <= 0) {
                throw new ValidationException("Quantity must be greater than 0 for item "
                        + oi.getItem().getId());
            }
            if (oi.getItem() == null || oi.getItem().getId() == null) {
                throw new ValidationException("Each order item must reference a valid item ID");
            }
        }

        if (order.getUser().getId() == null) {
            throw new ValidationException("Order must be linked to a user");
        }
    }

}



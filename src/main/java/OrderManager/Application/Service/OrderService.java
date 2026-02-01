package OrderManager.Application.Service;

import OrderManager.Application.Port.out.InventoryReservationPort;
import OrderManager.Application.Port.out.OrderPersistencePort;
import OrderManager.Application.Port.out.ReservationResult;
import OrderManager.Domain.Model.*;
import OrderManager.Exception.EntityNotFoundException;
import OrderManager.Exception.InsufficientInventoryException;
import OrderManager.Exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final int MONEY_SCALE = 2;
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

    public void UpdateOrder(UUID orderId, Order order){

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
    }

    private Order DeductAmountFromInventory(Order order){
        for (OrderItem orderItem : order.getItems()){
            orderItem.getItem().setQuantity(
                    orderItem.getItem().getQuantity() - orderItem.getQuantity()
            );
        }
        return order;
    }




    @Transactional
    public Order createOrder(Order draft) {
        hydrateItems(draft);               // load canonical items by ID
        validateDraft(draft);              // qty > 0, duplicates, etc.

        // 1) Try to reserve stock atomically
        var result = inventoryReservationPort.reserveItems(draft.getItems());
        if (result instanceof ReservationResult.Failure f) {
            throw new InsufficientInventoryException(f.lines());
        }
        var reservationId = ((ReservationResult.Success) result).reservationId();

        try {
            // 2) Price it
            applyPricing(draft);

            // 3) Persist the order with the reservation reference
            draft.setReservation(inventoryReservationPort.FindReservationById(reservationId));
            draft.setStatus(Utilities.Status.Pending);
            return orderPort.save(draft);
        } catch (RuntimeException ex) {
            // compensation if pricing/persisting fails
            inventoryReservationPort.releaseReservation(reservationId);
            throw ex;
        }
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
        BigDecimal discount = clamp01(safe(item.getDiscount()));
        BigDecimal qty = BigDecimal.valueOf(oi.getQuantity());
        BigDecimal unitNet = price.multiply(BigDecimal.ONE.subtract(discount));
        return unitNet.multiply(qty);
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private BigDecimal clamp01(BigDecimal v) {
        if (v.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;
        if (v.compareTo(BigDecimal.ONE) > 0) return BigDecimal.ONE;
        return v;
    }


    private void hydrateItems(Order order) {
        for (OrderItem oi : order.getItems()) {
            UUID itemId = oi.getItem().getId();

            // Pull the "real" Item from the ItemService / ItemRepository via your port
            Item item = itemService.GetItemById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Item", itemId));

            // Replace the shallow Item in the OrderItem with the hydrated one
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



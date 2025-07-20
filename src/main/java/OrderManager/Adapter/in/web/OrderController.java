package OrderManager.Controllers;

import OrderManager.Domain.Model.Order;
import OrderManager.Application.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> CreateOrder(@RequestBody Order order){
        Optional<Order> result = orderService.CreateOrder(order);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping
    public ResponseEntity<Order> UpdateOrder(@RequestBody Order order){
        Optional<Order> result = orderService.CreateOrder(order);
        return result.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping("{Id}")
    public ResponseEntity<Order> GetOrderById(@PathVariable("Id") UUID orderId){
        Optional<Order> order = orderService.GetOrderById(orderId);

        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Order>> GetAllOrders(){
        Optional<List<Order>> orders = orderService.GetAllOrders();
        return orders.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getbyuserid{Id}")
    public ResponseEntity<List<Order>> GetByUserId(@PathVariable("Id") UUID userId){
        Optional<List<Order>> orders = orderService.GetByUserId(userId);

        return orders.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("{Id}")
    public ResponseEntity<Boolean> DeleteOrder(@PathVariable("Id") UUID uuid){
        boolean isDeleted = orderService.DeleteOrder(uuid);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    final private AddressController addressController = new AddressController();
    //final private OrderListAndOrderController orderListAndOrderController = new OrderListAndOrderController();
    final private List<Order> orderList = new ArrayList<>();


    //MoveToItemController
    /*public void AddItemToPendingOrder(OrderItem orderItem, User loggedInUser) {
        Order selectedOrder = getOrderByUser(loggedInUser);
        if (selectedOrder == null) {
            selectedOrder = new Order();
            selectedOrder.setUserId(loggedInUser.getId());
            selectedOrder.addItem(orderItem);
            CreateOrder(selectedOrder);
        } else {
            selectedOrder.addItem(orderItem);
            UpdateOrder(selectedOrder);
        }
    }
    public void PrintCart(User loggedInUser) {
        Order order = getOrderByUser(loggedInUser);
        if (order != null) {
            List<OrderItem> items = order.getItems();
            if (items == null || items.isEmpty()) {
                System.out.println("Your cart is empty.");
                return;
            }

            System.out.println("Items in your cart:");
            for (OrderItem item : items) {
                System.out.println("Item ID: " + item.getItemId());
                System.out.println("Quantity: " + item.getQuantity());
                System.out.println("Total Price: $" + item.getTotalPrice());
                System.out.println("---------------------------");
            }
        }
    }*/
    /*public void PlacePendingOrder(UUID userId, List<OrderItem> items) {
        // Create a new order with a unique ID and provided userId
        Order newOrder = new Order();
        newOrder.setId(UUID.randomUUID());  // Generate a new unique order ID
        newOrder.setUserId(userId);
        *//*UserAddress address = addressController.getFirstAddressByUserId(userId);
        if(address == null)
            System.out.println("No Address Available For That User");
        *//*newOrder.setAddressId(UUID.fromString("2D1BA3E0-67A0-4195-8606-3E6231808F90"));
        newOrder.setDriverId(UUID.fromString("47931899-d415-48d6-a21b-97b1e1884a21"));
        newOrder.setStatus(Utilities.Status.Approved);
        newOrder.setDeliveryStatus(Utilities.DeliveryStatus.Pending);// Set the order status to (Confirmed)
        newOrder.setItems(items);
        newOrder.setNotes("n");// Add the provided list of OrderItems

        // Calculate order totals based on the items
        double subTotal = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        double deliveryFee = calculateDeliveryFee(subTotal);  // Define logic for delivery fee if needed
        double tax = calculateTax(subTotal);  // Define logic for tax if needed
        double totalPrice = subTotal + deliveryFee + tax;

        // Set calculated totals on the new order
        newOrder.setSubTotal(subTotal);
        newOrder.setDeliveryFee(deliveryFee);
        newOrder.setTax(tax);
        newOrder.setTotalPrice(totalPrice);

        // Save the new order and its items to the database
        saveOrderToDatabase(newOrder);

        System.out.println("New order placed successfully for user: " + userId);
    }
    // Helper methods for calculating delivery fee and tax
    private double calculateDeliveryFee(double subTotal) {
        return subTotal > 100 ? 0 : 5;  // Example: Free delivery for orders over $100
    }

    private double calculateTax(double subTotal) {
        return subTotal * 0.1;  // Example: 10% tax
    }

    private int getDeliveryStatusId(Utilities.DeliveryStatus deliveryStatus) {
        switch (deliveryStatus) {
            case Pending:
                return 1;
            case onWay:
                return 2;
            case Delivered:
                return 3;
            default:
                return 0;
        }
    }
    private int getStatusId(Utilities.Status status) {
        switch (status) {
            case Pending:
                return 1;
            case Confirmed:
                return 2;
            case Reviewing:
                return 3;
            case Approved:
                return 4;
            case OutForDelivery:
                return 5;
            case Canceled:
                return 6;
            case OnHold:
                return 7;
            case Returned:
                return 8;
            case Refunded:
                return 9;
        }
        return 1;
    }

    private Order buildOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        Utilities.Status statusType = getStatusType(Integer.parseInt(resultSet.getString("statusId")));
        Utilities.DeliveryStatus deliveryStatusType = getDeliveryStatusType(Integer.parseInt(resultSet.getString("deliveryStatusId")));

        order.setId(UUID.fromString(resultSet.getString("id")));
        order.setUserId(UUID.fromString(resultSet.getString("userId")));
        order.setAddressId(resultSet.getString("addressId") != null ? UUID.fromString(resultSet.getString("addressId")) : null);
        order.setDriverId(resultSet.getString("driverId") != null ? UUID.fromString(resultSet.getString("driverId")) : null);
        order.setStatus(statusType);
        order.setDeliveryStatus(deliveryStatusType);
        order.setSubTotal(resultSet.getDouble("subTotal"));
        order.setDeliveryFee(resultSet.getDouble("deliveryFee"));
        order.setTax(resultSet.getDouble("tax"));
        order.setTotalPrice(resultSet.getDouble("totalPrice"));
        order.setNotes(resultSet.getString("notes"));

        order.setItems(orderListAndOrderController.GetOrderListByOrderId(order.getId()));
        return order;
    }

    private Utilities.Status getStatusType(int statusId) {
        switch (statusId) {
            case 1:
                return Utilities.Status.Pending;
            case 2:
                return Utilities.Status.Confirmed;
            case 3:
                return Utilities.Status.Reviewing;
            case 4:
                return Utilities.Status.Approved;
            case 5:
                return Utilities.Status.OutForDelivery;
            case 6:
                return Utilities.Status.Canceled;
            case 7:
                return Utilities.Status.OnHold;
            case 8:
                return Utilities.Status.Returned;
            case 9:
                return Utilities.Status.Refunded;
        }
        return Utilities.Status.Pending;
    }
    private Utilities.DeliveryStatus getDeliveryStatusType(int deliveryStatusId) {
        switch (deliveryStatusId) {
            case 1:
                return Utilities.DeliveryStatus.Pending;
            case 2:
                return Utilities.DeliveryStatus.onWay;
            case 3:
                return Utilities.DeliveryStatus.Delivered;
        }
        return Utilities.DeliveryStatus.Pending;
    }
*/
}

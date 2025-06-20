package OrderManager.Controllers;

import OrderManager.Database.DatabaseConnection;
import OrderManager.Entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderController {
    final private AddressController addressController = new AddressController();
    final private OrderListAndOrderController orderListAndOrderController = new OrderListAndOrderController();
    final private List<Order> orderList = new ArrayList<>();

    public void DeleteOrder(UUID orderId) {
        String sql = "DELETE FROM Orders WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, orderId.toString());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Order deleted successfully!");
            } else {
                System.out.println("Order not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting order from database.");
        }
    }


    public List<Order> GetOrdersByUserId(UUID userId){

        String sql = "SELECT id FROM Orders WHERE userId = ?";
        List<Order> orders = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, userId.toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID orderId = UUID.fromString(resultSet.getString("id")); // Retrieve order ID
                Order order = GetOrderById(orderId); // Fetch the order using getOrderById
                if (order != null) {
                    orders.add(order); // Add the order to the list
                }
            }


        }catch (SQLException e){
            System.out.println("Error retrieving order by ID from database.");
        }
        return orders;

    }



    public Order GetOrderById(UUID orderId) {
        String sql = "SELECT * FROM Orders WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, orderId.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return buildOrderFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving order by ID from database.");
        }
        return null;
    }

    public void AddItemToPendingOrder(OrderItem orderItem, User loggedInUser) {
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
    }

    public void UpdateOrder(Order selectedOrder) {
        String sql = "UPDATE Orders SET userId = ?, addressId = ?, driverId = ?, statusId = ?, deliveryStatusId = ?, subTotal = ?, deliveryFee = ?, tax = ?, totalPrice = ?, notes = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            int statusId = getStatusId(selectedOrder.getStatus());
            int deliveryStatusId = getDeliveryStatusId(selectedOrder.getDeliveryStatus());
            statement.setString(1, selectedOrder.getUserId().toString());
            statement.setString(2, selectedOrder.getAddressId().toString());
            statement.setString(3, selectedOrder.getDriverId().toString());
            statement.setInt(4, statusId);
            statement.setInt(5, deliveryStatusId);
            statement.setDouble(6, selectedOrder.getSubTotal());
            statement.setDouble(7, selectedOrder.getDeliveryFee());
            statement.setDouble(8, selectedOrder.getTax());
            statement.setDouble(9, selectedOrder.getTotalPrice());
            statement.setString(10, selectedOrder.getNotes());
            statement.setString(11, selectedOrder.getId().toString());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Order updated successfully!");
            } else {
                System.out.println("Order not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating order in database.");
        }
    }

    public List<Order> GetOrders(){
        loadOrders();
        return orderList;
    }

    public void CreateOrder(Order order) {
        String sql = "INSERT INTO Orders (id, userId, addressId, driverId, status, deliveryStatus, subTotal, deliveryFee, tax, totalPrice, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, order.getId().toString());
            statement.setString(2, order.getUserId().toString());
            statement.setString(3, order.getAddressId() != null ? order.getAddressId().toString() : null);
            statement.setString(4, order.getDriverId() != null ? order.getDriverId().toString() : null);
            statement.setString(5, order.getStatus().name());
            statement.setString(6, order.getDeliveryStatus().name());
            statement.setDouble(7, order.getSubTotal());
            statement.setDouble(8, order.getDeliveryFee());
            statement.setDouble(9, order.getTax());
            statement.setDouble(10, order.getTotalPrice());
            statement.setString(11, order.getNotes());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Order created successfully!");
            } else {
                System.out.println("Failed to create order.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating order in database.");
        }
    }
    public void PlacePendingOrder(UUID userId, List<OrderItem> items) {
        // Create a new order with a unique ID and provided userId
        Order newOrder = new Order();
        newOrder.setId(UUID.randomUUID());  // Generate a new unique order ID
        newOrder.setUserId(userId);
        /*UserAddress address = addressController.getFirstAddressByUserId(userId);
        if(address == null)
            System.out.println("No Address Available For That User");
        */newOrder.setAddressId(UUID.fromString("2D1BA3E0-67A0-4195-8606-3E6231808F90"));
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

    private Order getOrderByUser(User loggedInUser) {
        String sql = "SELECT * FROM Orders WHERE userId = ? AND status = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, loggedInUser.getId().toString());
            statement.setString(2, Utilities.Status.Pending.name());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return buildOrderFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error loading order by user.");
        }
        return null;
    }

    private double calculateTax(double subTotal) {
        return subTotal * 0.1;  // Example: 10% tax
    }

    // Method to save order to database
    private void saveOrderToDatabase(Order order) {
        String orderSql = "INSERT INTO Orders (id, userId, addressId, driverId, " +
                "statusId, deliveryStatusId, subTotal, deliveryFee, tax, totalPrice, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String orderItemSql = "INSERT INTO OrderItem (id, orderId, itemId, " +
                "quantity, totalPrice) VALUES (?, ?, ?, ?, ?)";
        int statusId = getStatusId(order.getStatus());
        int deliveryStatusId = getDeliveryStatusId(order.getDeliveryStatus());
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Save the order
            try (PreparedStatement orderStmt = connection.prepareStatement(orderSql)) {
                orderStmt.setString(1, order.getId().toString());
                orderStmt.setString(2, order.getUserId().toString());
                orderStmt.setString(3, order.getAddressId().toString());
                orderStmt.setString(4, order.getDriverId().toString());
                orderStmt.setInt(5, statusId);
                orderStmt.setInt(6, deliveryStatusId);

                orderStmt.setDouble(7, order.getSubTotal());
                orderStmt.setDouble(8, order.getDeliveryFee());
                orderStmt.setDouble(9, order.getTax());
                orderStmt.setDouble(10, order.getTotalPrice());
                orderStmt.setString(11, order.getNotes());
                orderStmt.executeUpdate();
            }

            // Save each order item
            try (PreparedStatement itemStmt = connection.prepareStatement(orderItemSql)) {
                for (OrderItem item : order.getItems()) {
                    itemStmt.setString(1, item.getId().toString());
                    itemStmt.setString(2, order.getId().toString());
                    itemStmt.setString(3, item.getItemId().toString());
                    itemStmt.setInt(4, item.getQuantity());
                    itemStmt.setDouble(5, item.getTotalPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch(); // Execute batch insert for all items
            }

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            System.out.println("Error saving order to database.");
        }
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


    private void loadOrders() {
        String sql = "SELECT * FROM Orders";
        orderList.clear();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Order order = buildOrderFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error loading orders from database.");
        }
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

    private void addItemsToOrder(Order order, List<OrderItem> orderItems){

    }
}

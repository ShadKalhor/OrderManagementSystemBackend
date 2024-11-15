package Controllers;

import Entities.*;
import OrderManager.Database.DatabaseConnection;
import org.apache.tomcat.jni.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderController {
    private AddressController addressController = new AddressController();
    private List<Order> orderList = new ArrayList<>();

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
            e.printStackTrace();
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
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
        String sql = "UPDATE Orders SET userId = ?, addressId = ?, driverId = ?, status = ?, deliveryStatus = ?, subTotal = ?, deliveryFee = ?, tax = ?, totalPrice = ?, notes = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, selectedOrder.getUserId().toString());
            statement.setString(2, selectedOrder.getAddressId().toString());
            statement.setString(3, selectedOrder.getDriverId().toString());
            statement.setString(4, selectedOrder.getStatus().name());
            statement.setString(5, selectedOrder.getDeliveryStatus().name());
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
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }
    }
    public void PlacePendingOrder(UUID userId, List<OrderItem> items) {
        // Create a new order with a unique ID and provided userId
        Order newOrder = new Order();
        newOrder.setId(UUID.randomUUID());  // Generate a new unique order ID
        newOrder.setUserId(userId);
        UserAddress address = addressController.getFirstAddressByUserId(userId);
        newOrder.setAddressId(address.getId());
        newOrder.setDriverId(UUID.fromString("47931899-d415-48d6-a21b-97b1e1884a21"));
        newOrder.setStatus(Utilities.Status.Approved);
        newOrder.setDeliveryStatus(Utilities.DeliveryStatus.Pending);// Set the order status to Confirmed
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
            e.printStackTrace();
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


    private Order buildOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(UUID.fromString(resultSet.getString("id")));
        order.setUserId(UUID.fromString(resultSet.getString("userId")));
        order.setAddressId(resultSet.getString("addressId") != null ? UUID.fromString(resultSet.getString("addressId")) : null);
        order.setDriverId(resultSet.getString("driverId") != null ? UUID.fromString(resultSet.getString("driverId")) : null);
        order.setStatus(Utilities.Status.valueOf(resultSet.getString("status")));
        order.setDeliveryStatus(Utilities.DeliveryStatus.valueOf(resultSet.getString("deliveryStatus")));
        order.setSubTotal(resultSet.getDouble("subTotal"));
        order.setDeliveryFee(resultSet.getDouble("deliveryFee"));
        order.setTax(resultSet.getDouble("tax"));
        order.setTotalPrice(resultSet.getDouble("totalPrice"));
        order.setNotes(resultSet.getString("notes"));
        return order;
    }
}

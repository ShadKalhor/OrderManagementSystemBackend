package OrderManager.Controllers;

import OrderManager.Entities.OrderItem;
import OrderManager.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderListAndOrderController {

    public List<OrderItem> GetOrderListByOrderId(UUID selectedOrderId){
        String sql = "SELECT orderItemIds FROM OrderListAndOrder WHERE orderId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, selectedOrderId.toString());

            ResultSet resultSet = statement.executeQuery();

            return buildOrderFromResultSet(resultSet);

        } catch (SQLException e) {
            System.out.println("Error retrieving order by ID from database.");
        }
        return null;

    }

    private List<OrderItem> buildOrderFromResultSet(ResultSet resultSet) {
        List<OrderItem> orderItems = null;

        try {

            while (resultSet.next()) {

                String ids = resultSet.getString("orderItemIds");
                if (ids != null && !ids.isEmpty()) {
                    String[] idArray = ids.split(",");
                    for (String id : idArray) {
                        UUID orderId = UUID.fromString(id.trim());
                        OrderItem orderItem = fetchOrderItemById(orderId);
                        if (orderItem != null) {
                            if (orderItems == null)
                                orderItems = new ArrayList<>();
                            orderItems.add(orderItem);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in Processing ResultSet");
        }
        return orderItems;

    }
    private OrderItem fetchOrderItemById(UUID orderItemId) {
        String query = "SELECT id, itemId, quantity, totalPrice FROM OrderItem WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, orderItemId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                UUID itemId = UUID.fromString(rs.getString("itemId"));
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("totalPrice");

                return new OrderItem(id, itemId, quantity, totalPrice);
            }

        } catch (SQLException e) {
            System.out.println("Error Fetching Item via Id");
        }
        return null;
    }

}

package Controllers;

import Entities.Item;
import OrderManager.Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemController {

    public void CreateItem(Item item) {
        if (isValidItem(item)) {
            String sql = "INSERT INTO Item (id, name, description, price, size, discount, isAvailable, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, item.getName());
                statement.setString(3, item.getDescription());
                statement.setDouble(4, item.getPrice());
                statement.setString(5, item.getSize());
                statement.setDouble(6, item.getDiscount());
                statement.setBoolean(7, item.isAvailable());
                statement.setInt(8, item.getQuantity());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Item created successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error creating item in database.");
                e.printStackTrace();
            }
        }
    }

    private boolean isValidItem(Item item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            System.out.println("Invalid name.");
            return false;
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            System.out.println("Invalid description.");
            return false;
        }
        if (item.getPrice() < 0) {
            System.out.println("Price cannot be negative.");
            return false;
        }
        if (item.getSize() == null || item.getSize().isEmpty()) {
            System.out.println("Invalid size.");
            return false;
        }
        if (item.getDiscount() < 0 || item.getDiscount() > 1) {
            System.out.println("Discount must be between 0 and 1.");
            return false;
        }
        if (item.getQuantity() < 0) {
            System.out.println("Quantity cannot be negative.");
            return false;
        }
        return true;
    }

    public Item GetItemById(UUID itemId) {
        String sql = "SELECT * FROM Item WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, itemId.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return buildItemFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving item by ID from database.");
            e.printStackTrace();
        }
        return null;
    }

    public void UpdateItem(UUID itemId, Item item) {
        if (isValidItem(item)) {
            String sql = "UPDATE Item SET name = ?, description = ?, price = ?, size = ?, discount = ?, isAvailable = ?, quantity = ? WHERE id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, item.getName());
                statement.setString(2, item.getDescription());
                statement.setDouble(3, item.getPrice());
                statement.setString(4, item.getSize());
                statement.setDouble(5, item.getDiscount());
                statement.setBoolean(6, item.isAvailable());
                statement.setInt(7, item.getQuantity());
                statement.setString(8, itemId.toString());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Item updated successfully!");
                } else {
                    System.out.println("Item not found.");
                }
            } catch (SQLException e) {
                System.out.println("Error updating item in database.");
                e.printStackTrace();
            }
        }
    }

    public void DeleteItem(UUID itemId) {
        String sql = "DELETE FROM Item WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, itemId.toString());
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Item deleted successfully!");
            } else {
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting item from database.");
            e.printStackTrace();
        }
    }

    public List<Item> ListItems() {
        List<Item> itemList = new ArrayList<>();
        String sql = "SELECT * FROM Item";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Item item = buildItemFromResultSet(resultSet);
                itemList.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error loading items from database.");
            e.printStackTrace();
        }

        if (itemList.isEmpty()) {
            System.out.println("No items available.");
        }
        return itemList;
    }

    private Item buildItemFromResultSet(ResultSet resultSet) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("id"));
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        double price = resultSet.getDouble("price");
        String size = resultSet.getString("size");
        double discount = resultSet.getDouble("discount");
        boolean isAvailable = resultSet.getBoolean("isAvailable");
        int quantity = resultSet.getInt("quantity");

        return new Item(id, name, description, price, size, discount, isAvailable, quantity);
    }

    public Item LoadItemById(UUID itemId) {
        String sql = "SELECT * FROM Item WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, itemId.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return buildItemFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading item by ID from database.");
            e.printStackTrace();
        }
        return null; // Return null if no item is found with the given ID
    }

}

package Controllers;

import Entities.Driver;
import OrderManager.Database.DatabaseConnection;
import Extensions.RegexFormats;

import java.sql.*;
import java.util.UUID;

public class DriverController {

    public void createDriver(Driver driver) {
        String query = "INSERT INTO Driver (id, name, phone, vehicleNumber, age) VALUES (?, ?, ?, ?, ?)";
        if (isValidDriver(driver)) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, driver.getId().toString());
                statement.setString(2, driver.getName());
                statement.setString(3, driver.getPhone());
                statement.setString(4, driver.getVehicleNumber());
                statement.setInt(5, driver.getAge());

                statement.executeUpdate();
                System.out.println("Driver created successfully.");

            } catch (SQLException e) {
                System.out.println("Error creating driver in database.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid driver data.");
        }
    }

    public Driver readDriver(UUID driverId) {
        String query = "SELECT * FROM Driver WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, driverId.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Driver(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("vehicleNumber"),
                        resultSet.getInt("age")
                );
            } else {
                System.out.println("Driver not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error reading driver from database.");
            e.printStackTrace();
        }
        return null;
    }

    public void updateDriver(UUID driverId, String name, String phone, String vehicleNumber, int age) {
        String query = "UPDATE Driver SET name = ?, phone = ?, vehicleNumber = ?, age = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, vehicleNumber);
            statement.setInt(4, age);
            statement.setString(5, driverId.toString());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Driver updated successfully!");
            } else {
                System.out.println("Driver not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating driver in database.");
            e.printStackTrace();
        }
    }

    public void deleteDriver(UUID driverId) {
        String query = "DELETE FROM Driver WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, driverId.toString());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Driver deleted successfully!");
            } else {
                System.out.println("Driver not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting driver from database.");
            e.printStackTrace();
        }
    }

    private boolean isValidDriver(Driver driver) {
        RegexFormats formats = new RegexFormats();
        if (driver.getName() == null || driver.getName().isEmpty() || !driver.getName().matches(formats.NameRegex)) {
            System.out.println("Invalid Name.");
            return false;
        }
        return true;
    }
}

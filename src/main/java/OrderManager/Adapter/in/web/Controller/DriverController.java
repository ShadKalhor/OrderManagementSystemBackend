/*
package OrderManager.Adapter.in.web.Controller;

import OrderManager.Database.DatabaseConnection;
import OrderManager.Shared.Extensions.RegexFormats;
import org.springframework.web.bind.annotation.*;
import OrderManager.Domain.Model.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Driver")
public class DriverController {


    @PostMapping("/Create")
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
    @GetMapping("/GetDriver")
    public Driver GetDriver(UUID driverId) {
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

    @GetMapping("/GetDrivers")
    public List<Driver> GetDrivers(){
        String sql = "SELECT * FROM Driver";
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Driver driver = buildOrderFromResultSet(resultSet);
                drivers.add(driver);
            }
        } catch (SQLException e) {
            System.out.println("Error loading orders from database.");
            e.printStackTrace();
        }
        return drivers;
    }

    @PatchMapping("/Update")
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

    @DeleteMapping("/Delete{driverId}")
    public void deleteDriver(@PathVariable UUID driverId) {
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

    private Driver buildOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(UUID.fromString(resultSet.getString("id")));
        driver.setName(resultSet.getString("name"));
        driver.setPhone(resultSet.getString("phone"));
        driver.setVehicleNumber(resultSet.getString("vehicleNumber"));
        driver.setAge(resultSet.getInt("age"));
        return driver;
    }
}
*/

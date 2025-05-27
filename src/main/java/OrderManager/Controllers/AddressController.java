package OrderManager.Controllers;

import OrderManager.Database.DatabaseConnection;
import OrderManager.Entities.UserAddress;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Address")
public class AddressController {

    @PostMapping("/Create")
    public void createAddress(UserAddress address) {
        String sql = "INSERT INTO UserAddress (id, userId, name, city, description, type, street, residentialNo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, address.getUserId().toString());
            statement.setString(3, address.getName());
            statement.setString(4, address.getCity());
            statement.setString(5, address.getDescription());
            statement.setString(6, address.getType());
            statement.setString(7, address.getStreet());
            statement.setString(8, address.getResidentialNo());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Address created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating address in database.");
        }
    }

   /* public UserAddress getFirstAddressByUserId(UUID userId) {
        String sql = "SELECT TOP 1 * FROM dbo.UserAddress WHERE userId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userId.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return buildAddressFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving address by user ID from database.");
            e.printStackTrace();
        }
        return null;
    }
*/

    @GetMapping("/GetAddressById")
    public UserAddress getAddressById(UUID id) {
        String sql = "SELECT * FROM UserAddress WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return buildAddressFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving address by ID from database.");
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/GetAllAddressesById")
    public List<UserAddress> listAddressesByUserId(UUID userId) {
        List<UserAddress> addresses = new ArrayList<>();
        String sql = "SELECT * FROM UserAddress WHERE userId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userId.toString());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                addresses.add(buildAddressFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving addresses by user ID from database.");
            e.printStackTrace();
        }
        return addresses;
    }


    @PatchMapping("/Update")
    public void updateAddress(UserAddress address) {
        String sql = "UPDATE UserAddress SET userId = ?, name = ?, city = ?, description = ?, type = ?, street = ?, residentialNo = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, address.getUserId().toString());
            statement.setString(2, address.getName());
            statement.setString(3, address.getCity());
            statement.setString(4, address.getDescription());
            statement.setString(5, address.getType());
            statement.setString(6, address.getStreet());
            statement.setString(7, address.getResidentialNo());
            statement.setString(8, address.getId().toString());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Address updated successfully.");
            } else {
                System.out.println("Address not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating address in database.");
            e.printStackTrace();
        }
    }


    @DeleteMapping("/Delete")
    public void deleteAddress(UUID id) {
        String sql = "DELETE FROM UserAddress WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id.toString());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Address deleted successfully.");
            } else {
                System.out.println("Address not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting address from database.");
            e.printStackTrace();
        }
    }


    private UserAddress buildAddressFromResultSet(ResultSet resultSet) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("id"));
        UUID userId = UUID.fromString(resultSet.getString("userId"));
        String name = resultSet.getString("name");
        String city = resultSet.getString("city");
        String description = resultSet.getString("description");
        String type = resultSet.getString("type");
        String street = resultSet.getString("street");
        String residentialNo = resultSet.getString("residentialNo");

        return new UserAddress(id, userId, name, city, description, type, street, residentialNo);
    }
}

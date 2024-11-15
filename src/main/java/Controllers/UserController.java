package Controllers;

import Entities.User;
import Entities.Utilities;
import Extensions.RegexFormats;
import OrderManager.Database.DatabaseConnection;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.ResultSet;

public class UserController {


    public void CreateUser(User user) {
        if(isValideUser(user)){
            saveUserToDatabase(user);
        }else{
            System.out.println("Invalid user Information");
        }
    }

    public void CreateUser(UUID id, UUID roleId, String name, String phone, String password, Utilities.Genders gender) {
        User user = new User(id, roleId, name, phone, password, gender);
        if(isValideUser(user)){
            saveUserToDatabase(user);
        }else{
            System.out.println("Invalid user Information");
        }
    }
    private boolean isValideUser(User user) {
        RegexFormats formats = new RegexFormats();


        if(isDublicatePhone(user.getPhone()))
            return false;
        if (user.getRoleId() == null) {

            System.out.println("1");
            return false;
        }

        // Validating Name
        if (user.getName() == null || user.getName().isEmpty() || !(user.getName().matches(formats.NameRegex))) {

            System.out.println("2");
            return false;
        }

        // Validating Phone Number
        if (user.getPhone() == null || user.getPhone().isEmpty() || !(user.getPhone().matches(formats.PhoneRegex))) {

            System.out.println("3");
            return false;
        }

        // Validating Password
        if (user.getPassword() == null || user.getPassword().isEmpty() || !(user.getPassword().matches(formats.PasswordRegex))) {

            System.out.println("4");
            return false;
        }

        return true;


    }


    private boolean isDublicatePhone(String phone) {
        Gson gson = new Gson();
        List<User> users;

        users = loadUsers();
        if(users.isEmpty()){
            return false;
        }

        // Check if any user has the same phone number
        return users.stream()
                .anyMatch(user -> phone.equals(user.getPhone()));

    }

    private List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, roleId, name, phone, password, genderId FROM Users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Retrieve values from the result set
                UUID id = UUID.fromString(resultSet.getString("id"));
                UUID roleId = resultSet.getString("roleId") != null ? UUID.fromString(resultSet.getString("roleId")) : null;
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String password = resultSet.getString("password");
                int genderId = resultSet.getInt("genderId");

                // Convert genderId to the corresponding enum value
                Utilities.Genders gender = genderId == 1 ? Utilities.Genders.Male : Utilities.Genders.Female;

                // Create a new User object and add it to the list
                User user = new User(id, roleId, name, phone, password, gender);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error loading users from the database.");
            e.printStackTrace();
        }

        return users;

    }

    public void UpdateUser(User updatedUser) {
        if (isValideUser(updatedUser)) {
            String sql = "UPDATE Users SET roleId = ?, name = ?, phone = ?, password = ?, genderId = ? WHERE id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                // Set the parameters in the SQL statement
                statement.setString(1, updatedUser.getRoleId() != null ? updatedUser.getRoleId().toString() : null);
                statement.setString(2, updatedUser.getName());
                statement.setString(3, updatedUser.getPhone());
                statement.setString(4, updatedUser.getPassword());
                statement.setInt(5, updatedUser.getGender().ordinal() + 1); // Assuming Male=1, Female=2 in Genders table
                statement.setString(6, updatedUser.getId().toString());

                // Execute the update statement
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("User updated successfully!");
                } else {
                    System.out.println("User not found.");
                }
            } catch (SQLException e) {
                System.out.println("Error updating user in the database.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid user data.");
        }
    }

    public void DeleteUser(UUID userId) {
        String sql = "DELETE FROM Users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the ID parameter in the SQL statement
            statement.setString(1, userId.toString());

            // Execute the delete statement
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user from the database.");
            e.printStackTrace();
        }
    }

    public User GetUserById(UUID userId) {
        String sql = "SELECT id, roleId, name, phone, password, genderId FROM Users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the userId parameter in the SQL query
            statement.setString(1, userId.toString());

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve values from the result set
                    UUID id = UUID.fromString(resultSet.getString("id"));
                    UUID roleId = resultSet.getString("roleId") != null ? UUID.fromString(resultSet.getString("roleId")) : null;
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    String password = resultSet.getString("password");
                    int genderId = resultSet.getInt("genderId");

                    // Convert genderId to the corresponding enum value
                    Utilities.Genders gender = genderId == 1 ? Utilities.Genders.Male : Utilities.Genders.Female;

                    // Create and return the User object
                    return new User(id, roleId, name, phone, password, gender);
                } else {
                    System.out.println("User not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user from database.");
            e.printStackTrace();
        }

        return null;  // Return null if user is not found or an error occurs
    }
    public User GetUserByPhoneNumber(String phoneNumber) {
        String sql = "SELECT id, roleId, name, phone, password, genderId FROM Users WHERE phone = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the phone parameter in the SQL query
            statement.setString(1, phoneNumber);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve values from the result set
                    UUID id = UUID.fromString(resultSet.getString("id"));
                    UUID roleId = resultSet.getString("roleId") != null ? UUID.fromString(resultSet.getString("roleId")) : null;
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    String password = resultSet.getString("password");
                    int genderId = resultSet.getInt("genderId");

                    // Convert genderId to the corresponding enum value
                    Utilities.Genders gender = genderId == 1 ? Utilities.Genders.Male : Utilities.Genders.Female;

                    // Create and return the User object
                    return new User(id, roleId, name, phone, password, gender);
                } else {
                    System.out.println("User not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user from database.");
            e.printStackTrace();
        }

        return null;
    }

    public boolean IsValidLogin(String phoneNumber, String password) {
        String sql = "SELECT COUNT(*) FROM Users WHERE phone = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the parameters for phone number and password
            statement.setString(1, phoneNumber);
            statement.setString(2, password);

            // Execute the query and check if a matching user exists
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if(count > 0)
                        return true;
                    else
                        return false;// Returns true if a match is found
                }
            }
        } catch (SQLException e) {
            System.out.println("Error validating login credentials.");
            e.printStackTrace();
        }
        return false;  // Returns false if no match is found or an error occurs
    }

    public List<User> LoadUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, roleId, name, phone, password, genderId FROM Users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Retrieve values from the result set
                UUID id = UUID.fromString(resultSet.getString("id"));
                UUID roleId = resultSet.getString("roleId") != null ? UUID.fromString(resultSet.getString("roleId")) : null;
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String password = resultSet.getString("password");
                int genderId = resultSet.getInt("genderId");

                // Convert genderId to the corresponding enum value
                Utilities.Genders gender = genderId == 1 ? Utilities.Genders.Male : Utilities.Genders.Female;

                // Create a new User object and add it to the list
                User user = new User(id, roleId, name, phone, password, gender);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error loading users from the database.");
            e.printStackTrace();
        }

        return users;
    }


    public void PrintUsers() {
        List<User> users = loadUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Users:");
        for (User user : users) {
            System.out.println("ID: " + user.getId());
            System.out.println("Role ID: " + (user.getRoleId() != null ? user.getRoleId() : "No role assigned"));
            System.out.println("Name: " + user.getName());
            System.out.println("Phone: " + user.getPhone());
            System.out.println("Password: " + user.getPassword());
            System.out.println("Gender: " + user.getGender());
            System.out.println("-----------------------------------");
        }
    }


    private void    saveUserToDatabase(User user){
        String sql = "INSERT INTO Users (id, roleId, name, phone, password, genderId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the parameters in the SQL statement
            statement.setString(1, user.getId().toString());
            statement.setString(2, user.getRoleId() != null ? user.getRoleId().toString() : "45C4CAD3-D78D-4E76-93D2-7DA1C3900969");
            statement.setString(3, user.getName());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getPassword());
            statement.setInt(6, user.getGender().ordinal() + 1); // Assuming Male=1, Female=2 in Genders table

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User saved successfully!");
            } else {
                System.out.println("Failed to save the user.");
            }
        } catch (SQLException e) {
            System.out.println("Error saving user to database.");
            e.printStackTrace();
        }
    }


}

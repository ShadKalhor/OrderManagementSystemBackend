package OrderManager.Controllers;

import OrderManager.Entities.User;
import OrderManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> CreateUser(@RequestBody User user){
        Optional<User> savedUser =  userService.SaveUser(user);
        return savedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping
    public ResponseEntity<User> UpdateUser(@RequestBody User user){

        Optional<User> savedUser =  userService.SaveUser(user);
        return savedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @GetMapping("/getbyid{id}")
    public ResponseEntity<User> GetById(@PathVariable("id") UUID uuid){
        Optional<User> user = userService.GetUserById(uuid);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getall")
    public ResponseEntity<List<User>> GetAllUsers(){
        Optional<List<User>> users =Optional.of(userService.GetAllUsers());
        return users.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
    @DeleteMapping("{Id}")
    public ResponseEntity<Boolean> DeleteUser(@PathVariable("Id") UUID uuid){
        Boolean isDeleted = userService.DeleteUser(uuid);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }




/*

    @PostMapping("/Create")
    public String CreateUser(@RequestBody User user) {
        if(isValidUser(user,true)){
            return saveUserToDatabase(user);
        }else{
            System.out.println("Invalid user Information");
            return "Invalid user Information";

        }
    }


    @PatchMapping("/Update")
    public String UpdateUser(@RequestBody User updatedUser) {
        if (isValidUser(updatedUser, false)) {
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
                    return "User updated successfully!";
                } else {
                    System.out.println("User not found.");
                    return "User not found.";
                }
            } catch (SQLException e) {
                System.out.println("Error updating user in the database.");
                return "Error updating user in the database.";
            }
        } else {
            System.out.println("Invalid user data.");
            return "Invalid user data.";
        }
    }


    @DeleteMapping("/Delete{id}")
    public String DeleteUser(@PathVariable UUID userId) {
        String sql = "DELETE FROM Users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the ID parameter in the SQL statement
            statement.setString(1, userId.toString());

            // Execute the delete statement
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully!");
                return "User deleted successfully!";

            } else {
                System.out.println("User not found.");
                return "User not found.";
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user from the database.");
            return "Error deleting user from the database.";
        }
    }

    //Make the return type optional
    @GetMapping("getById{id}")
    public ResponseEntity<User> GetUserById(@PathVariable UUID userId) {

    }

    @GetMapping("getByPhone{phoneNumber}")
    public User GetUserByPhoneNumber(@PathVariable String phoneNumber) {
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
        }

        return null;
    }

    @GetMapping("isValidLogin{phoneNumber,password}")
    public boolean IsValidLogin(@PathVariable("phoneNumber") String phoneNumber,@PathVariable("Password") String password) {
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

                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error validating login credentials.");
        }
        return false;  // Returns false if no match is found or an error occurs
    }

    @GetMapping("loadUsers")
    public ResponseEntity<List<User>> LoadUsers() {
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
        }

        return  ResponseEntity.ok(users);
    }
*/


}

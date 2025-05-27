package OrderManager.Service;

import OrderManager.Entities.User;
import OrderManager.Entities.Utilities;
import OrderManager.Database.DatabaseConnection;
import OrderManager.Extensions.RegexFormats;
import OrderManager.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;


    public Optional<User> GetUserById(UUID userId){
        return userRepository.findById(userId);
    }
    public List<User> GetAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> SaveUser(User user) {
        if(!isValidUser(user) && !isDuplicatePhone(user))
            return Optional.ofNullable(userRepository.save(user));
        return Optional.empty();
    }
    public boolean DeleteUser(UUID userId) {
        Optional<User> user = GetUserById(userId);
        if(user.isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    private boolean isValidUser(User user) {
        RegexFormats formats = new RegexFormats();


        if (user.getRoleId() == null) {

            return false;
        }


        if (user.getName() == null || user.getName().isEmpty()
                || !(user.getName().matches(formats.NameRegex))) {
            return false;
        }


        if (user.getPhone() == null || user.getPhone().isEmpty()
                || !(user.getPhone().matches(formats.PhoneRegex))) {
            return false;
        }

        return user.getPassword() != null && !user.getPassword().isEmpty()
                && user.getPassword().matches(formats.PasswordRegex);


    }

    private boolean isDuplicatePhone(User user) {

        return Optional.ofNullable(loadUsers())
                .filter(u -> !u.isEmpty())
                .map(u -> u.stream().anyMatch(p -> user.getPhone().equals(p.getPhone()) && !(user.getId().equals(p.getId()))))
                .orElse(false);
    }

    private List<User> loadUsers() {
        List<User> users = null;
        String sql = "SELECT id, roleId, name, phone, password, genderId FROM Users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                UUID roleId = resultSet.getString("roleId") != null ? UUID.fromString(resultSet.getString("roleId")) : null;
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String password = resultSet.getString("password");
                int genderId = resultSet.getInt("genderId");

                Utilities.Genders gender = genderId == 1 ? Utilities.Genders.Male : Utilities.Genders.Female;

                User user = new User(id, roleId, name, phone, password, gender);
                users = new ArrayList<>();
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error loading users from the database.");
        }

        return users;

    }



}

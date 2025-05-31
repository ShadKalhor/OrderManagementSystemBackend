package OrderManager.Service;

import OrderManager.DTO.UserDTO;
import OrderManager.Entities.Gender;
import OrderManager.Entities.User;
import OrderManager.Entities.UserRole;
import OrderManager.Entities.Utilities;
import OrderManager.Database.DatabaseConnection;
import OrderManager.Extensions.RegexFormats;
import OrderManager.Repository.GenderRepository;
import OrderManager.Repository.UserRepository;
import OrderManager.Repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private GenderRepository genderRepository;


    public Optional<User> GetUserById(UUID userId){
        return userRepository.findById(userId);
    }

    public List<User> GetAllUsers(){
        return userRepository.findAll();
    }
/*
    public Optional<User> SaveUser(UserDTO dto){
        User user = new User();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());

        System.out.println("Looking up role with ID: " + dto.getRoleId());
        System.out.println("Role ID class: " + dto.getRoleId().getClass().getName());

        UserRole role = userRoleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + dto.getRoleId()));
        user.setRole(role);

        Gender gender = genderRepository.findById(dto.getGenderId())
                .orElseThrow(() -> new EntityNotFoundException("Gender not found with ID: " + dto.getGenderId()));
        user.setGender(gender);

        if (isValidUser(user) && !isDuplicatePhone(user)) {
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();

    }*/

    public Optional<User> GetUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<User> UpdateUser(User user){

        user = checkInput(user);

        if(isValidUser(user))
            return Optional.of(userRepository.save(user));
        return Optional.empty();

    }

    public Optional<User> SaveUser(User user) {

        user = checkInput(user);

        if(isValidUser(user) && !isDuplicatePhone(user))
            return Optional.of(userRepository.save(user));
        return Optional.empty();
    }

    private User checkInput(User user) {
        if(user.getGender().getName() == null || user.getGender().getName().isEmpty())
            user.setGender(genderRepository.findById(user.getGender().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Gender not found with ID: " + user.getGender().getId()))
            );

        else if (user.getGender().getId() == 0)
            user.setGender(genderRepository.GetByName(user.getGender().getName()));


        if (user.getRole().getRoleName() == null || user.getRole().getRoleName().isEmpty()) {
            user.setRole(
                    userRoleRepository.findById(user.getRole().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + user.getRole().getId()))
            );
        }

        else if(user.getRole().getId() == null || user.getRole().getId().equals(new UUID(0L, 0L)))
            user.setRole(userRoleRepository.GetByRoleName(user.getRole().getRoleName()));

        if(user.getId() == null || user.getId().equals(new UUID(0L, 0L)))
            user.setId(UUID.randomUUID());

        return user;

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


        if (user.getRole() == null) {

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

        return Optional.of(userRepository.findAll())
                .filter(u -> !u.isEmpty())
                .map(u -> u.stream().anyMatch(p -> user.getPhone().equals(p.getPhone()) && !(user.getId().equals(p.getId()))))
                .orElse(false);
    }
}

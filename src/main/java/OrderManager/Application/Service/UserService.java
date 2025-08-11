package OrderManager.Application.Service;

import OrderManager.Application.Port.out.AddressPersistencePort;
import OrderManager.Application.Port.out.UserPersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import OrderManager.Extensions.RegexFormats;
import OrderManager.Domain.Model.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    private final UserPersistencePort userPort;

    
    public UserService(UserPersistencePort userPort){
        this.userPort = userPort;
    }

    public Optional<User> GetUserById(UUID userId){
        return userPort.findById(userId);
    }

    public List<User> GetAllUsers(){
        return userPort.findAll();
    }

    public Optional<User> GetUserByPhoneNumber(String phoneNumber) {
        return userPort.findByPhoneNumber(phoneNumber);
    }

    public Optional<User> SaveUser(User user) {

        user = checkInput(user);

        if(isValidUser(user) && !isDuplicatePhone(user))
            return Optional.of(userPort.save(user));
        return Optional.empty();
    }

    public boolean DeleteUser(UUID userId) {
        Optional<User> user = GetUserById(userId);
        if(user.isPresent()) {
            userPort.deleteById(userId);
            return true;
        }
        return false;
    }


    private User checkInput(User user) {
        if(user.getId() == null || user.getId().equals(new UUID(0L, 0L)))
            user.setId(UUID.randomUUID());

        return user;

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

        return Optional.of(userPort.findAll())
                .filter(u -> !u.isEmpty())
                .map(u -> u.stream().anyMatch(p -> user.getPhone().equals(p.getPhone()) && !(user.getId().equals(p.getId()))))
                .orElse(false);
    }
}

package ordermanager.infrastructure.service;

import io.vavr.control.Option;
import ordermanager.domain.port.out.UserPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserPersistencePort userPort;

    
    public UserService(UserPersistencePort userPort, PasswordEncoder passwordEncoder){
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    public Option<User> GetUserById(UUID userId){
        return userPort.findById(userId);
    }

    public List<User> GetAllUsers(){
        return userPort.findAll();
    }

    public Option<User> GetUserByPhoneNumber(String phoneNumber) {
        return userPort.findByPhone(phoneNumber);
    }

    public Option<User> SaveUser(User user) {

        user = checkInput(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userPort.save(user);
    }

    public boolean DeleteUser(UUID userId) {
        return userPort.findById(userId)
                .map(d -> {
                    userPort.deleteById(userId);
                    return true;
                })
                .getOrElse(false);
    }


    private User checkInput(User user) {
        if(user.getId() == null || user.getId().equals(new UUID(0L, 0L)))
            user.setId(UUID.randomUUID());

        return user;

    }
    private boolean isDuplicatePhone(User user) {

        return Option.of(userPort.findAll())
                .filter(u -> !u.isEmpty())
                .map(u -> u.stream().anyMatch(p -> user.getPhone().equals(p.getPhone()) && !(user.getId().equals(p.getId()))))
                .getOrElse(false);
    }
}
package ordermanager.infrastructure.service;

import io.vavr.control.Option;
import ordermanager.domain.port.out.UserPersistencePort;
import ordermanager.infrastructure.exception.EntityNotFoundException;
import ordermanager.infrastructure.store.persistence.entity.Item;
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

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userPort.save(user);
    }

    public Option<User> UpdateUser(UUID userId, User user){

        Option<User> itemExists = GetUserById(userId);
        if(itemExists.isEmpty())
            throw new EntityNotFoundException("User", userId);
        user.setId(userId);
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


}
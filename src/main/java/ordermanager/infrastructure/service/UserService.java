package ordermanager.infrastructure.service;

import io.vavr.control.Either;

import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.UserDomain;
import ordermanager.domain.port.out.UserPersistencePort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserPersistencePort userPort;

    
    public UserService(UserPersistencePort userPort, PasswordEncoder passwordEncoder){
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    public Either<StructuredError, UserDomain> GetUserById(UUID userId){
        return userPort.findById(userId).toEither(() -> new StructuredError("Could Not Find User With Specified UserId", ErrorType.NOT_FOUND_ERROR));
    }

    public List<UserDomain> GetAllUsers(){
        return userPort.findAll();
    }

    public Either<StructuredError, UserDomain> GetUserByPhoneNumber(String phoneNumber) {
        return userPort.findByPhone(phoneNumber).toEither(new StructuredError("Could Not Find User With Specified Phone Number", ErrorType.NOT_FOUND_ERROR));
    }

    public Either<StructuredError, UserDomain> CreateUser(UserDomain user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userPort.save(user);
    }

    public Either<StructuredError, UserDomain> UpdateUser(UUID userId, UserDomain user){

        return GetUserById(userId).flatMap(existing ->{
            user.setId(userId);
            return userPort.save(user);
        });
    }

    public Either<StructuredError, Void> DeleteUser(UUID userId) {
        return userPort.findById(userId).toEither(new StructuredError("Could Not Find User With Specified Id", ErrorType.NOT_FOUND_ERROR)).map(existing -> {
            userPort.deleteById(userId);
            return null;
        });
    }


}
package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.port.out.UserPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.User;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserPersistencePort {

    private final SpringDataUserRepository userRepository;


    public UserRepositoryAdapter(SpringDataUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Option<User> findById(UUID userId) {
                return userRepository.findOptionById(userId);
    }

    @Override
    public Option<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Option<User> findByPhone(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber);
    }

    @Override
    public Either<StructuredError, User> save(User user) {

        return Try.of(() -> userRepository.save(user)).toEither(new StructuredError("Could Not Save User", ErrorType.SERVER_ERROR));
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID userId) {
        return Try.run(() -> userRepository.deleteById(userId)).toEither(new StructuredError("Could Not Delete User", ErrorType.SERVER_ERROR));
    }
}

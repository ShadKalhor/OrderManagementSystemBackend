package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.UserDomain;
import ordermanager.domain.port.out.UserPersistencePort;
import ordermanager.infrastructure.mapper.UserMapper;
import ordermanager.infrastructure.store.persistence.entity.User;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserPersistencePort {

    private final SpringDataUserRepository userRepository;
    private UserMapper userMapper;

    public UserRepositoryAdapter(SpringDataUserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Option<UserDomain> findById(UUID userId) {
                return userRepository.findOptionById(userId).map(userMapper::toDomain);
    }

    @Override
    public Option<UserDomain> findByName(String name) {
        return userRepository.findByName(name).map(userMapper::toDomain);
    }

    @Override
    public List<UserDomain> findAll() {
        return userRepository.findAll()
                .stream().map(userMapper::toDomain).toList();
    }

    @Override
    public Option<UserDomain> findByPhone(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber).map(userMapper::toDomain);
    }

    @Override
    public Either<StructuredError, UserDomain> save(UserDomain domain) {
        User user = userMapper.toEntity(domain);
        return Try.of(() -> userRepository.save(user)).map(userMapper::toDomain).toEither(new StructuredError("Could Not Save User", ErrorType.SERVER_ERROR));
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID userId) {
        return Try.run(() -> userRepository.deleteById(userId)).toEither(new StructuredError("Could Not Delete User", ErrorType.SERVER_ERROR));
    }
}

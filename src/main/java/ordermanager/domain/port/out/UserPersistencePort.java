package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.infrastructure.store.persistence.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserPersistencePort {
    Option<User> findById(UUID userId);

    Option<User> findByName(String name);
    List<User> findAll();

    Option<User> findByPhone(String phoneNumber);

    Either<StructuredError, User> save(User user);

    Either<StructuredError, Void> deleteById(UUID userId);
}

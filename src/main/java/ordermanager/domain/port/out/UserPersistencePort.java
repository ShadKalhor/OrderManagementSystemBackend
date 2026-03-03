package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.UserDomain;
import ordermanager.infrastructure.store.persistence.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserPersistencePort {
    Option<UserDomain> findById(UUID userId);

    Option<UserDomain> findByName(String name);
    List<UserDomain> findAll();

    Option<UserDomain> findByPhone(String phoneNumber);

    Either<StructuredError, UserDomain> save(UserDomain user);

    Either<StructuredError, Void> deleteById(UUID userId);
}

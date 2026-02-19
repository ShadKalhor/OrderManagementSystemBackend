package ordermanager.domain.port.out;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    Option<User> findById(UUID userId);

    Option<User> findByName(String name);
    List<User> findAll();

    Option<User> findByPhone(String phoneNumber);

    Option<User> save(User user);

    void deleteById(UUID userId);
}

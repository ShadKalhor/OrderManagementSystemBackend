package ordermanager.domain.port.out;

import ordermanager.infrastructure.store.persistence.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    Optional<User> findById(UUID userId);

    Optional<User> findByName(String name);
    List<User> findAll();

    Optional<User> findByPhone(String phoneNumber);

    User save(User user);

    void deleteById(UUID userId);
}

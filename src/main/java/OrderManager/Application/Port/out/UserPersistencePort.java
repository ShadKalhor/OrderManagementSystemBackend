package OrderManager.Application.Port.out;

import OrderManager.Domain.Model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    Optional<User> findById(UUID userId);

    List<User> findAll();

    Optional<User> findByPhoneNumber(String phoneNumber);

    User save(User user);

    void deleteById(UUID userId);
}

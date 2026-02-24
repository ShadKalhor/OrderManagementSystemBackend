package ordermanager.infrastructure.store.persistence.jpa;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<User, UUID> {

    Option<User> findByPhone(String phoneNumber);
    Option<User> findByName(String name);
    Option<User> findOptionById(UUID uuid);
}

package ordermanager.adapter.out.persistence;

import ordermanager.infrastructure.store.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(String phoneNumber);
    Optional<User> findByName(String name);
}

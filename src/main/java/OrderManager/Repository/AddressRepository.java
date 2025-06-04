package OrderManager.Repository;

import OrderManager.Entities.User;
import OrderManager.Entities.UserAddress;
import org.apache.tomcat.jni.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<UserAddress, UUID> {

    Optional<List<UserAddress>> findAddressesByUser(User user);

    Optional<List<UserAddress>> findAddressesByUserId(UUID userId);

}

package ordermanager.application.port.out;

import ordermanager.domain.model.UserAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressPersistencePort {
    
    Optional<List<UserAddress>> findAddressesByUserId(UUID userId);

    Optional<UserAddress> save(UserAddress userAddress);

    Optional<UserAddress> findById(UUID uuid);

    void deleteById(UUID uuid);
}

package ordermanager.domain.port.out;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressPersistencePort {
    
    Option<List<UserAddress>> findAddressesByUserId(UUID userId);

    Option<UserAddress> save(UserAddress userAddress);

    Option<UserAddress> findById(UUID uuid);

    void deleteById(UUID uuid);
}

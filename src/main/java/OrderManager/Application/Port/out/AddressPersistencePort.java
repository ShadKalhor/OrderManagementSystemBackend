package OrderManager.Application.Port.out;

import OrderManager.Domain.Model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressPersistencePort {
    
    Optional<List<UserAddress>> findAddressesByUserId(UUID userId);

    Optional<UserAddress> save(UserAddress userAddress);

    Optional<UserAddress> findById(UUID uuid);

    void deleteById(UUID uuid);
}

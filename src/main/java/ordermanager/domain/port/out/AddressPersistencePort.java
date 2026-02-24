package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;

import java.util.List;
import java.util.UUID;

public interface AddressPersistencePort {
    
    List<UserAddress> findAddressesByUserId(UUID userId);

    Either<StructuredError, UserAddress> save(UserAddress userAddress);

    Option<UserAddress> findById(UUID uuid);

    Either<StructuredError, Void> deleteById(UUID uuid);
}

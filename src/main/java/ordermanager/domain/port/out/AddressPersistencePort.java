package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.UserAddressDomain;

import java.util.List;
import java.util.UUID;

public interface AddressPersistencePort {
    
    List<UserAddressDomain> findAddressesByUserId(UUID userId);

    Either<StructuredError, UserAddressDomain> save(UserAddressDomain userAddress);

    Option<UserAddressDomain> findById(UUID uuid);

    Either<StructuredError, Void> deleteById(UUID uuid);
}

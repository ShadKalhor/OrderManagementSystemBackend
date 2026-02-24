package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.port.out.AddressPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AddressRepositoryAdapter implements AddressPersistencePort {

    SpringDataAddressRepository addressRepository;

    public AddressRepositoryAdapter(SpringDataAddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    @Override
    public List<UserAddress> findAddressesByUserId(UUID userId) {

        return addressRepository.findAddressesByUserId(userId);

    }

    @Override
    public Either<StructuredError, UserAddress> save(UserAddress userAddress) {
        return Try.of(() -> addressRepository.save(userAddress)).toEither().mapLeft(throwable -> new StructuredError("Failed while storing address", ErrorType.SERVER_ERROR));
    }

    @Override
    public Option<UserAddress> findById(UUID uuid) {
        return addressRepository.findOptionById(uuid);
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID uuid) {
        return Try.run(() -> addressRepository.deleteById(uuid))
                .toEither()
                .mapLeft(throwable -> new StructuredError("Delete failed", ErrorType.NOT_FOUND_ERROR))
                .map(nothing -> (Void) null);
    }
}

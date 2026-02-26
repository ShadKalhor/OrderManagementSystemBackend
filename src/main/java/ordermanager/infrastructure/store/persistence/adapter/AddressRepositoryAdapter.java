package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.UserAddressDomain;
import ordermanager.domain.port.out.AddressPersistencePort;

import ordermanager.infrastructure.mapper.UserAddressMapper;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataAddressRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AddressRepositoryAdapter implements AddressPersistencePort {

    SpringDataAddressRepository addressRepository;
    UserAddressMapper addressMapper;

    public AddressRepositoryAdapter(SpringDataAddressRepository addressRepository, UserAddressMapper addressMapper){
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public List<UserAddressDomain> findAddressesByUserId(UUID userId) {

        List<UserAddress> addresses = addressRepository.findAddressesByUserId(userId);
        return addresses.stream().map(addressMapper::toDomain).toList();

    }

    @Override
    public Either<StructuredError, UserAddressDomain> save(UserAddressDomain userAddressDomain) {
        UserAddress addressEntity = addressMapper.toEntity(userAddressDomain);

        return Try.of(() -> {
            UserAddress address = addressRepository.save(addressEntity);
            return addressMapper.toDomain(address);
        })
                .toEither().mapLeft(throwable -> new StructuredError("Failed while storing address", ErrorType.SERVER_ERROR));
    }

    @Override
    public Option<UserAddressDomain> findById(UUID uuid) {
        return addressRepository.findOptionById(uuid).map(addressMapper::toDomain);
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID uuid) {
        return Try.run(() -> addressRepository.deleteById(uuid))
                .toEither()
                .mapLeft(throwable -> new StructuredError("Delete failed", ErrorType.NOT_FOUND_ERROR))
                .map(nothing -> (Void) null);
    }
}

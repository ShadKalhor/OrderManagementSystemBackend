package ordermanager.infrastructure.service;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.UserAddressDomain;
import ordermanager.domain.model.UserDomain;
import ordermanager.domain.port.out.AddressPersistencePort;
import ordermanager.domain.port.out.UserPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    
    private final AddressPersistencePort addressPort;
    private final UserPersistencePort userPort;

    public Either<StructuredError, UserAddressDomain> CreateAddress(UserAddressDomain userAddress){

        Option<UserDomain> userDomain = userPort.findById(userAddress.getUserId());

        if(userDomain.isEmpty())
            return Either.left(new StructuredError("User Not Found", ErrorType.NOT_FOUND_ERROR));

        return addressPort.save(userAddress);
    }

    public Either<StructuredError, UserAddressDomain> UpdateAddress(UUID addressId, UserAddressDomain userAddress){

        return GetAddressById(addressId).flatMap(existing -> {
                    userAddress.setId(addressId);
                    return addressPort.save(userAddress);
                });
    }


    public List<UserAddressDomain> GetUserAddresses(UUID userId){
        return addressPort.findAddressesByUserId(userId);
    }

    public Either<StructuredError, UserAddressDomain> GetAddressById(UUID uuid){
        return addressPort.findById(uuid).toEither(new StructuredError("Address Not Found", ErrorType.NOT_FOUND_ERROR));
    }

    public Either<StructuredError, Void> DeleteAddress(UUID addressId){

        return addressPort.findById(addressId)
                .toEither(new StructuredError(
                        "Address not found",
                        ErrorType.NOT_FOUND_ERROR
                ))
                .map(address -> {
                    addressPort.deleteById(addressId);
                    return null;
                });
    }

}

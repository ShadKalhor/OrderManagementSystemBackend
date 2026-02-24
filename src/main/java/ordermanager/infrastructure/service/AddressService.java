package ordermanager.infrastructure.service;

import io.vavr.control.Either;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.port.out.AddressPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {
    
    private final AddressPersistencePort addressPort;

    public AddressService(AddressPersistencePort addressPort){
        this.addressPort = addressPort;
    }

    public Either<StructuredError, UserAddress> CreateAddress(UserAddress userAddress){
        return addressPort.save(userAddress);
    }

    public Either<StructuredError, UserAddress> UpdateAddress(UUID addressId, UserAddress userAddress){

        return GetAddressById(addressId).flatMap(existing -> {
                    userAddress.setId(addressId);
                    return addressPort.save(userAddress);
                });
    }


    public List<UserAddress> GetUserAddresses(UUID userId){
        return addressPort.findAddressesByUserId(userId);
    }

    public Either<StructuredError, UserAddress> GetAddressById(UUID uuid){
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

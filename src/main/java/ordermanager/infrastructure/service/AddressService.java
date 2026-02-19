package ordermanager.infrastructure.service;

import io.vavr.control.Option;
import ordermanager.domain.port.out.AddressPersistencePort;
import ordermanager.infrastructure.exception.EntityNotFoundException;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {
    
    private final AddressPersistencePort addressPort;

    public AddressService(AddressPersistencePort addressPort){
        this.addressPort = addressPort;
    }

    public Option<UserAddress> CreateAddress(UserAddress userAddress){
        return addressPort.save(userAddress);
    }

    public Option<UserAddress> UpdateAddress(UUID addressId, UserAddress userAddress){
        Option<UserAddress> addressExists = GetAddressById(addressId);
        if (addressExists.isEmpty())
            throw new EntityNotFoundException("Address",addressId);
        userAddress.setId(addressId);
        return addressPort.save(userAddress);
    }


    public Option<List<UserAddress>> GetUserAddresses(UUID userId){
        return addressPort.findAddressesByUserId(userId);
    }

    public Option<UserAddress> GetAddressById(UUID uuid){
        return addressPort.findById(uuid);
    }


    public boolean DeleteAddress(UUID addressId){
        return addressPort.findById(addressId)
                .map(d -> {
                    addressPort.deleteById(addressId);
                    return true;
                }).getOrElse(false);

    }

}

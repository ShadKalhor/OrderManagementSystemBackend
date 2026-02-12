package OrderManager.Application.Service;

import OrderManager.Application.Port.out.*;
import OrderManager.Domain.Model.*;
import OrderManager.Exception.*;
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

    public Optional<UserAddress> CreateAddress(UserAddress userAddress){
        userAddress.setId(UUID.randomUUID());
        return addressPort.save(userAddress);
    }

    public void UpdateAddress(UUID addressId, UserAddress userAddress){
        Optional<UserAddress> addressExists = GetAddressById(addressId);
        if (addressExists.isEmpty())
            throw new EntityNotFoundException("Address",addressId);
        userAddress.setId(addressId);
        addressPort.save(userAddress);
    }


    public Optional<List<UserAddress>> GetUserAddresses(UUID userId){
        return addressPort.findAddressesByUserId(userId);
    }

    public Optional<UserAddress> GetAddressById(UUID uuid){
        return addressPort.findById(uuid);
    }


    public boolean DeleteAddress(UUID addressId){
        return addressPort.findById(addressId)
                .map(d -> {
                    addressPort.deleteById(addressId);
                    return true;
                })
                .orElse(false);

    }

}

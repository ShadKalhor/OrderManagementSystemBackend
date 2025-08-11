package OrderManager.Application.Service;

import OrderManager.Application.Port.out.AddressPersistencePort;
import OrderManager.Domain.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {
    
    private AddressPersistencePort addressPort;

    public AddressService(AddressPersistencePort addressPort){
        this.addressPort = addressPort;
    }

    public Optional<UserAddress> CreateAddress(UserAddress userAddress){

        Optional<UserAddress> result = Optional.of(addressPort.save(userAddress));
        if (result != null)
            return result;
        return Optional.empty();
    }


/*
    public Optional<List<UserAddress>> GetUserAddresses(User user){
        Optional<List<UserAddress>> addresses = addressPort.findAddressesByUser(user);
        return addresses;
    }*/

    public Optional<List<UserAddress>> GetUserAddresses(UUID userId){
        Optional<List<UserAddress>> addresses = addressPort.findAddressesByUserId(userId);
        return addresses;
    }

    public Optional<UserAddress> GetAddressById(UUID uuid){
        Optional<UserAddress> result = addressPort.findById(uuid);
        if (result != null)
            return result;
        return Optional.empty();
    }


    public boolean DeleteAddress(UUID uuid){
        Optional<UserAddress> address = GetAddressById(uuid);
        if(address.isPresent()) {
            addressPort.deleteById(uuid);
            return true;
        }
        return false;
    }

}

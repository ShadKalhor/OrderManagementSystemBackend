package OrderManager.Service;

import OrderManager.Entities.User;
import OrderManager.Entities.UserAddress;
import OrderManager.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;


    public Optional<UserAddress> CreateAddress(UserAddress userAddress){

        Optional<UserAddress> result = Optional.of(addressRepository.save(userAddress));
        if (result != null)
            return result;
        return Optional.empty();
    }



    public Optional<List<UserAddress>> GetUserAddresses(User user){
        Optional<List<UserAddress>> addresses = addressRepository.findAddressesByUser(user);
        return addresses;
    }

    public Optional<List<UserAddress>> GetUserAddresses(UUID userId){
        Optional<List<UserAddress>> addresses = addressRepository.findAddressesByUserId(userId);
        return addresses;
    }

    public Optional<UserAddress> GetAddressById(UUID uuid){
        Optional<UserAddress> result = addressRepository.findById(uuid);
        if (result != null)
            return result;
        return Optional.empty();
    }


    public boolean DeleteAddress(UUID uuid){
        Optional<UserAddress> address = GetAddressById(uuid);
        if(address.isPresent()) {
            addressRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

}

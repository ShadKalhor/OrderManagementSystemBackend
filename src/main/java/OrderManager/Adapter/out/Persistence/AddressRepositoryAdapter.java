package OrderManager.Adapter.out.Persistence;

import OrderManager.Application.Port.out.AddressPersistencePort;
import OrderManager.Domain.Model.UserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AddressRepositoryAdapter implements AddressPersistencePort {

    SpringDataAddressRepository addressRepository;

    public AddressRepositoryAdapter(SpringDataAddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    @Override
    public Optional<List<UserAddress>> findAddressesByUserId(UUID userId) {
        return addressRepository.findAddressesByUserId(userId);
    }

    @Override
    public Optional<UserAddress> save(UserAddress userAddress) {
        return Optional.of(addressRepository.save(userAddress));
    }

    @Override
    public Optional<UserAddress> findById(UUID uuid) {
        return addressRepository.findById(uuid);
    }

    @Override
    public void deleteById(UUID uuid) {
        addressRepository.deleteById(uuid);
    }
}

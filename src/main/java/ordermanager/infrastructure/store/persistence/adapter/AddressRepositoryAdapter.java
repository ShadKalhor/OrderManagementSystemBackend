package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.domain.port.out.AddressPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
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
    public Option<List<UserAddress>> findAddressesByUserId(UUID userId) {
        return addressRepository.findAddressesByUserId(userId);
    }

    @Override
    public Option<UserAddress> save(UserAddress userAddress) {
        return Option.of(addressRepository.save(userAddress));
    }

    @Override
    public Option<UserAddress> findById(UUID uuid) {
        return addressRepository.findOptionById(uuid);
    }

    @Override
    public void deleteById(UUID uuid) {
        addressRepository.deleteById(uuid);
    }
}

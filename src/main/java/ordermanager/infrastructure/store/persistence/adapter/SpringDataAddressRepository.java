package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataAddressRepository extends JpaRepository<UserAddress, UUID> {

    Option<List<UserAddress>> findAddressesByUserId(UUID userId);
    Option<UserAddress> findOptionById(UUID uuid);
}

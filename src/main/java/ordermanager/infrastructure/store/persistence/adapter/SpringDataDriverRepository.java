package ordermanager.infrastructure.store.persistence.adapter;


import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataDriverRepository extends JpaRepository<Driver, UUID> {

    Option<Driver> findOptionById(UUID uuid);

}

package ordermanager.domain.port.out;

import ordermanager.infrastructure.store.persistence.entity.Driver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverPersistencePort {


    Optional<Driver> save(Driver driver);

    Optional<Driver> findById(UUID driverId);

    List<Driver> findAll();

    void deleteById(UUID driverId);
}

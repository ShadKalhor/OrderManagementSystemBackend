package ordermanager.domain.port.out;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.Driver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverPersistencePort {


    Option<Driver> save(Driver driver);

    Option<Driver> findById(UUID driverId);

    List<Driver> findAll();

    void deleteById(UUID driverId);
}

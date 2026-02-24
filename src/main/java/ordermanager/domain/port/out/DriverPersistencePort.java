package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.infrastructure.store.persistence.entity.Driver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverPersistencePort {


    Either<StructuredError, Driver> save(Driver driver);

    Option<Driver> findById(UUID driverId);

    List<Driver> findAll();

    Either<StructuredError, Void> deleteById(UUID driverId);
}

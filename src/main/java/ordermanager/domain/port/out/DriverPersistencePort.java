package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.DriverDomain;

import java.util.List;
import java.util.UUID;

public interface DriverPersistencePort {


    Either<StructuredError, DriverDomain> save(DriverDomain driverDomain);

    Option<DriverDomain> findById(UUID driverId);

    List<DriverDomain> findAll();

    Either<StructuredError, Void> deleteById(UUID driverId);
}

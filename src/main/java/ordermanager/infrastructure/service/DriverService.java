package ordermanager.infrastructure.service;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.DriverDomain;
import ordermanager.domain.port.out.DriverPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.Driver;
import ordermanager.infrastructure.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService {


    private final DriverPersistencePort driverPort;

    public DriverService(DriverPersistencePort driverPort){
        this.driverPort = driverPort;
    }


    public Either<StructuredError, DriverDomain> CreateDriver(DriverDomain driver) {
        return driverPort.save(driver);
    }

    public Either<StructuredError, DriverDomain> UpdateDriver(UUID driverId, DriverDomain driver){


        return GetDriverById(driverId).flatMap(existing -> {
            driver.setId(driverId);
            return driverPort.save(driver);
        });

    }

    public Either<StructuredError, DriverDomain> GetDriverById(UUID driverId) {

        return driverPort.findById(driverId).toEither(new StructuredError("Driver Not Found", ErrorType.NOT_FOUND_ERROR));
    }

    public List<DriverDomain> GetAllDrivers() {
        return driverPort.findAll();
    }

    public Either<StructuredError, Void> DeleteDriver(UUID driverId) {
        return driverPort.findById(driverId)
                .toEither(new StructuredError(
                        "Driver not found",
                        ErrorType.NOT_FOUND_ERROR
                ))
                .map(address -> {
                        driverPort.deleteById(driverId);
                    return null;
                });
    }
}

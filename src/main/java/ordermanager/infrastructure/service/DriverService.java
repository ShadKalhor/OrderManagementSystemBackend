package ordermanager.infrastructure.service;

import io.vavr.control.Option;
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


    public Option<Driver> CreateDriver(Driver driver) {
        return driverPort.save(driver);
    }

    public Option<Driver> UpdateDriver(UUID driverId, Driver driver){
        Option<Driver> driverExists = GetDriverById(driverId);
        if (driverExists.isEmpty())
            throw new EntityNotFoundException("Driver", driverId);
        driver.setId(driverId);
        return driverPort.save(driver);
    }

    public Option<Driver> GetDriverById(UUID driverId) {
        return driverPort.findById(driverId);
    }

    public List<Driver> GetAllDrivers() {
        return driverPort.findAll();
    }

    public boolean DeleteDriver(UUID driverId) {
        return driverPort.findById(driverId)
                .map(d -> {
                    driverPort.deleteById(driverId);
                    return true;
                })
                .getOrElse(false);
    }
}

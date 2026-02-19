package ordermanager.infrastructure.service;

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


    public Optional<Driver> CreateDriver(Driver driver) {
        return driverPort.save(driver);
    }

    public void UpdateDriver(UUID driverId, Driver driver){

        Optional<Driver> driverExists = GetDriverById(driverId);
        if (driverExists.isEmpty())
            throw new EntityNotFoundException("Driver", driverId);
        driver.setId(driverId);
        driverPort.save(driver);
    }

    public Optional<Driver> GetDriverById(UUID driverId) {
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
                .orElse(false);
    }
}

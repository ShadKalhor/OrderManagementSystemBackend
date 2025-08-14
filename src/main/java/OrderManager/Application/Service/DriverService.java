package OrderManager.Application.Service;

import OrderManager.Application.Port.out.DriverPersistencePort;
import OrderManager.Domain.Model.Driver;
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

    public Optional<Driver> FindDriver(UUID driverId) {
        return driverPort.findById(driverId);
    }

    public List<Driver> FindAllDrivers() {
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

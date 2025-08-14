package OrderManager.Adapter.out.Persistence;

import OrderManager.Application.Port.out.DriverPersistencePort;
import OrderManager.Domain.Model.Driver;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DriverRepositoryAdapter implements DriverPersistencePort {
    private final SpringDataDriverRepository driverRepository;

    public DriverRepositoryAdapter(SpringDataDriverRepository driverRepository){
        this.driverRepository = driverRepository;
    }


    @Override
    public Optional<Driver> save(Driver driver) {
        return Optional.of(driverRepository.save(driver));
    }

    @Override
    public Optional<Driver> findById(UUID driverId) {
        return driverRepository.findById(driverId);
    }

    @Override
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    @Override
    public void deleteById(UUID driverId) {

        driverRepository.deleteById(driverId);

    }
}

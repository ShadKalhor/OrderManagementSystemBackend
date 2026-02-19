package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.domain.port.out.DriverPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.Driver;
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
    public Option<Driver> save(Driver driver) {
        return Option.of(driverRepository.save(driver));
    }

    @Override
    public Option<Driver> findById(UUID driverId) {
        return driverRepository.findOptionById(driverId);
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

package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.DriverDomain;
import ordermanager.domain.port.out.DriverPersistencePort;
import ordermanager.infrastructure.mapper.DriverMapper;
import ordermanager.infrastructure.store.persistence.entity.Driver;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataDriverRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DriverRepositoryAdapter implements DriverPersistencePort {

    private final SpringDataDriverRepository driverRepository;
    private DriverMapper driverMapper;

    public DriverRepositoryAdapter(SpringDataDriverRepository driverRepository, DriverMapper driverMapper){
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }


    @Override
    public Either<StructuredError, DriverDomain> save(DriverDomain driverDomain) {
        Driver driverEntity = driverMapper.toEntity(driverDomain);
        return Try.of(() -> {
            Driver driver = driverRepository.save(driverEntity);
            return driverMapper.toDomain(driver);
        })
                .toEither().mapLeft(throwable -> new StructuredError("Failed while storing driver", ErrorType.SERVER_ERROR));

    }

    @Override
    public Option<DriverDomain> findById(UUID driverId) {
        return driverRepository.findOptionById(driverId).map(driverMapper::toDomain);
    }

    @Override
    public List<DriverDomain> findAll() {
        List<Driver> drivers = driverRepository.findAll();
        return drivers.stream().map(driverMapper::toDomain).toList();
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID driverId) {
        return Try.run(() -> driverRepository.deleteById(driverId))
                .toEither()
                .mapLeft(throwable -> new StructuredError("Delete failed", ErrorType.NOT_FOUND_ERROR))
                .map(nothing -> (Void) null);
    }
}

package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
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
    public Either<StructuredError, Driver> save(Driver driver) {
        return Try.of(() -> driverRepository.save(driver)).toEither().mapLeft(throwable -> new StructuredError("Failed while storing driver", ErrorType.SERVER_ERROR));

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
    public Either<StructuredError, Void> deleteById(UUID driverId) {
        return Try.run(() -> driverRepository.deleteById(driverId))
                .toEither()
                .mapLeft(throwable -> new StructuredError("Delete failed", ErrorType.NOT_FOUND_ERROR))
                .map(nothing -> (Void) null);
    }
}

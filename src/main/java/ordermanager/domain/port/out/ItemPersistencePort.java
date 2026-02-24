package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.infrastructure.store.persistence.entity.Item;

import java.util.List;
import java.util.UUID;

public interface ItemPersistencePort {

    Option<Item> findById(UUID id);

    Either<StructuredError, Item> save(Item item);

    List<Item> findAll();

    Either<StructuredError, Void> deleteById(UUID itemId);
}

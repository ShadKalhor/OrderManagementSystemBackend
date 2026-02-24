package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.port.out.ItemPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.Item;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ItemRepositoryAdapter implements ItemPersistencePort {

    SpringDataItemRepository itemRepository;

    public ItemRepositoryAdapter(SpringDataItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public Option<Item> findById(UUID id) {
        return itemRepository.findOptionById(id);
    }

    @Override
    public Either<StructuredError, Item> save(Item item) {
        return Try.of(() -> itemRepository.save(item)).toEither().mapLeft(throwable -> new StructuredError("Could Not Save Item", ErrorType.SERVER_ERROR));
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID itemId) {
        return Try.run(() -> itemRepository.deleteById(itemId)).toEither()
                .mapLeft(throwable -> new StructuredError("Could Not Delete Item", ErrorType.SERVER_ERROR)).map(nothing -> (Void) nothing);
    }
}

package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.ItemDomain;
import ordermanager.domain.port.out.ItemPersistencePort;
import ordermanager.infrastructure.mapper.ItemMapper;
import ordermanager.infrastructure.store.persistence.entity.Item;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ItemRepositoryAdapter implements ItemPersistencePort {

    SpringDataItemRepository itemRepository;
    ItemMapper itemMapper;

    public ItemRepositoryAdapter(SpringDataItemRepository itemRepository, ItemMapper itemMapper){
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public Option<ItemDomain> findById(UUID id) {
        return itemRepository.findOptionById(id).map(itemMapper::toDomain);
    }

    @Override
    public Either<StructuredError, ItemDomain> save(ItemDomain item) {
        Item entity = itemMapper.toEntity(item);
        return Try.of(() -> itemRepository.save(entity)).map(itemMapper::toDomain).toEither().mapLeft(throwable -> new StructuredError("Could Not Save Item", ErrorType.SERVER_ERROR));
    }

    @Override
    public List<ItemDomain> findAll() {
        return itemRepository.findAll().stream().map(itemMapper::toDomain).toList();
    }

    @Override
    public Either<StructuredError, Void> deleteById(UUID itemId) {
        return Try.run(() -> itemRepository.deleteById(itemId)).toEither()
                .mapLeft(throwable -> new StructuredError("Could Not Delete Item", ErrorType.SERVER_ERROR)).map(nothing -> (Void) nothing);
    }
}

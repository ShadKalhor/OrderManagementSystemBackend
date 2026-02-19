package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.domain.port.out.ItemPersistencePort;
import ordermanager.infrastructure.store.persistence.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
    public Option<Item> save(Item item) {
        return Option.of(itemRepository.save(item));
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public void deleteById(UUID itemId) {

        itemRepository.deleteById(itemId);
    }
}

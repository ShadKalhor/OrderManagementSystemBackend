package ordermanager.adapter.out.persistence;

import ordermanager.application.port.out.ItemPersistencePort;
import ordermanager.domain.model.Item;
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
    public Optional<Item> findById(UUID id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
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

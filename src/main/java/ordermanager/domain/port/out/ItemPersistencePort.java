package ordermanager.domain.port.out;

import ordermanager.infrastructure.store.persistence.entity.Item;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemPersistencePort {

    Optional<Item> findById(UUID id);

    Item save(Item item);

    List<Item> findAll();

    void deleteById(UUID itemId);
}

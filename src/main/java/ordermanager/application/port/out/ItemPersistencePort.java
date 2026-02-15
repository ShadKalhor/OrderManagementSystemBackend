package ordermanager.application.port.out;

import ordermanager.domain.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemPersistencePort {

    Optional<Item> findById(UUID id);

    Item save(Item item);

    List<Item> findAll();

    void deleteById(UUID itemId);
}

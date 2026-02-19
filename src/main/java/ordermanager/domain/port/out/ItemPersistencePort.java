package ordermanager.domain.port.out;

import io.vavr.control.Option;
import ordermanager.infrastructure.store.persistence.entity.Item;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemPersistencePort {

    Option<Item> findById(UUID id);

    Option<Item> save(Item item);

    List<Item> findAll();

    void deleteById(UUID itemId);
}

package OrderManager.Application.Port.out;

import OrderManager.Domain.Model.Item;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemPersistencePort {

    Optional<Item> findById(UUID id);

    Item save(Item item);

    List<Item> findAll();

    void deleteById(UUID itemId);
}

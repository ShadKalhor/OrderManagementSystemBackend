package ordermanager.infrastructure.service;


import io.vavr.control.Option;
import ordermanager.domain.port.out.ItemPersistencePort;
import ordermanager.infrastructure.exception.EntityNotFoundException;
import ordermanager.infrastructure.store.persistence.entity.Item;
import ordermanager.infrastructure.store.persistence.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    private final ItemPersistencePort itemPort;

    
    public ItemService(ItemPersistencePort itemPort){
        this.itemPort = itemPort;
    }

    public Option<Item> CreateItem(Item item) {
        return itemPort.save(item);
    }

    public Option<Item> UpdateItem(UUID itemId, Item item){

        Option<Item> itemExists = GetItemById(itemId);
        if(itemExists.isEmpty())
            throw new EntityNotFoundException("Item", itemId);
        item.setId(itemId);
        return itemPort.save(item);
    }

    public Option<Item> GetItemById(UUID itemId) {
        return itemPort.findById(itemId);
    }


    public List<Item> GetAllItems() {
        return itemPort.findAll();
    }

    public boolean DeleteItem(UUID itemId) {

        return itemPort.findById(itemId)
                .map(d -> {
                    itemPort.deleteById(itemId);
                    return true;
                })
                .getOrElse(false);
    }

}

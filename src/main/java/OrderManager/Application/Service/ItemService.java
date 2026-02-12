package OrderManager.Application.Service;


import OrderManager.Application.Port.out.ItemPersistencePort;
import OrderManager.Domain.Model.*;
import OrderManager.Exception.EntityNotFoundException;
import OrderManager.Exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ItemService {

    private final ItemPersistencePort itemPort;

    
    public ItemService(ItemPersistencePort itemPort){
        this.itemPort = itemPort;
    }
    
    public Map<String, String> GetUnavailableItems(List<OrderItem> orderItems){
        Map<String, String> unavailableItems = null;

        for(OrderItem orderItem : orderItems){
            Optional<Item> result = itemPort.findById(orderItem.getItem().getId());

            if(result.isPresent()){
                Item item = result.get();
                if(item.getQuantity() < orderItem.getQuantity()) {
                    if (unavailableItems == null)
                        unavailableItems = new HashMap<>();
                    unavailableItems.put(item.getName(), "inventory does not have" +
                                orderItem.getQuantity() + " Of " + item.getName() + " Left.");
                }
            }
            else {
                if (unavailableItems == null)
                    unavailableItems = new HashMap<>();
                unavailableItems.put(orderItem.getItem().getId().toString(), "This order item with this item id is not available anymore.");
            }
        }
        return unavailableItems;
    }

    public Optional<Item> CreateItem(Item item) {
        return Optional.of(itemPort.save(item));
    }

    public void UpdateItem(UUID itemId, Item item){

        Optional<Item> itemExists = GetItemById(itemId);
        if(itemExists.isEmpty())
            throw new EntityNotFoundException("Item", itemId);
        item.setId(itemId);
        itemPort.save(item);
    }

    public Optional<Item> GetItemById(UUID itemId) {
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
                .orElse(false);
    }

}

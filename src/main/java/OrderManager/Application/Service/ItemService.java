package OrderManager.Application.Service;


import OrderManager.Application.Port.out.ItemPersistencePort;
import OrderManager.Application.Port.out.OrderItemPersistencePort;
import OrderManager.Domain.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    
    private OrderItemPersistencePort orderItemPort;
    private ItemPersistencePort itemPort;

    
    public ItemService(OrderItemPersistencePort orderitemPort, ItemPersistencePort itemPort){
        this.orderItemPort = orderitemPort;
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

    public Optional<Item> SaveItem(Item item) {
        Optional<Item> result;
        if (isValidItem(item))
            result = Optional.of(itemPort.save(item));
        else
            return Optional.empty();
        return result;
    }

    private boolean isValidItem(Item item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            System.out.println("Invalid name.");
            return false;
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            System.out.println("Invalid description.");
            return false;
        }
        if (item.getPrice() < 0) {
            System.out.println("Price cannot be negative.");
            return false;
        }
        if (item.getSize() == null || item.getSize().isEmpty()) {
            System.out.println("Invalid size.");
            return false;
        }
        if (item.getDiscount() < 0 || item.getDiscount() > 1) {
            System.out.println("Discount must be between 0 and 1.");
            return false;
        }
        if (item.getQuantity() < 0) {
            System.out.println("Quantity cannot be negative.");
            return false;
        }
        return true;
    }


    public Optional<Item> GetItemById(UUID itemId) {
        return itemPort.findById(itemId);
    }


    public Optional<List<Item>> GetAllItems() {
        return Optional.of(itemPort.findAll());
    }

    public boolean DeleteItem(UUID itemId) {

        Optional<Item> item = itemPort.findById(itemId);
        if (item.isPresent()){
            itemPort.deleteById(itemId);
            return true;
        }else
            return false;
    }

}

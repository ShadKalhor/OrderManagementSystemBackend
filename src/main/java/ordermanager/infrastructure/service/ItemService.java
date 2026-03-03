package ordermanager.infrastructure.service;


import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.ItemDomain;
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

    public Either<StructuredError, ItemDomain> CreateItem(ItemDomain item) {
        return itemPort.save(item);
    }

    public Either<StructuredError, ItemDomain> UpdateItem(UUID itemId, ItemDomain item){

        return GetItemById(itemId).flatMap(existing -> {
            item.setId(itemId);
            return itemPort.save(item);
        });

    }

    public Either<StructuredError, ItemDomain> GetItemById(UUID itemId) {

        return itemPort.findById(itemId).toEither(new StructuredError("Could Not Find Item", ErrorType.NOT_FOUND_ERROR));
    }



    public List<ItemDomain> GetAllItems() {
        return itemPort.findAll();
    }

    public Either<StructuredError, Void> DeleteItem(UUID itemId) {

        return itemPort.findById(itemId).toEither(new StructuredError("Could Not Find Item", ErrorType.NOT_FOUND_ERROR)).map(existing -> {
            itemPort.deleteById(itemId);
            return null;
        });

    }

}

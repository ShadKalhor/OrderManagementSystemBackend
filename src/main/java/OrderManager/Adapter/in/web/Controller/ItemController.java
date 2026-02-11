package OrderManager.Adapter.in.web.Controller;

import OrderManager.Domain.Model.Item;
import OrderManager.Application.Service.ItemService;
import OrderManager.Shared.Dto.ItemDtos.CreateItemRequest;
import OrderManager.Shared.Dto.ItemDtos.ItemResponse;
import OrderManager.Shared.Dto.ItemDtos.UpdateItemRequest;
import OrderManager.Shared.Mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/Item")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper){
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> CreateItem(@Valid @RequestBody CreateItemRequest itemBody){/*
        Optional<Item> result = itemService.SaveItem(item);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());*/

        var item = itemMapper.toDomain(itemBody);
        return itemService.CreateItem(item)
                .map(itemMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> UpdateItem(@PathVariable UUID itemId, @Valid @RequestBody UpdateItemRequest itemBody){
        /*Optional<Item> result = itemService.SaveItem(item);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());*/

        var itemExists = itemService.GetItemById(itemId).orElse(null);

        if (itemExists == null)
            return ResponseEntity.notFound().build();

        itemMapper.update(itemExists, itemBody);
        var updatedUser = itemService.CreateItem(itemExists);
        return updatedUser.map(item -> ResponseEntity.ok(itemMapper.toResponse(item))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> GetItemById(@PathVariable("itemId") UUID itemId){
        /*Optional<Item> result = itemService.GetItemById(itemId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
*/
        return itemService.GetItemById(itemId)
                .map(itemMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping()
    public ResponseEntity<List<ItemResponse>> GetAllItems(){/*
        Optional<List<Item>> items = itemService.GetAllItems();
        return items.map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());*/
        var items = itemService.GetAllItems().stream()
                .map(itemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> DeleteItem(@PathVariable("itemId") UUID itemId){
        boolean isDeleted = itemService.DeleteItem(itemId);
        if(isDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().build();
    }
}

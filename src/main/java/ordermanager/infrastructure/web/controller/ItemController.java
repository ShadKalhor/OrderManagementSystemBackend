package ordermanager.infrastructure.web.controller;

import ordermanager.infrastructure.service.ItemService;
import ordermanager.domain.dto.item.CreateItemRequest;
import ordermanager.domain.dto.item.ItemResponse;
import ordermanager.domain.dto.item.UpdateItemRequest;
import ordermanager.infrastructure.mapper.ItemMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper){
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> CreateItem(@Valid @RequestBody CreateItemRequest itemBody){

        var item = itemMapper.create(itemBody);
        return itemService.CreateItem(item)
                .map(itemMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> UpdateItem(@PathVariable UUID itemId, @Valid @RequestBody UpdateItemRequest itemBody){
        var item = itemMapper.update(itemBody);
        return itemService.UpdateItem(itemId, item)
                .map(itemMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> GetItemById(@PathVariable("itemId") UUID itemId){

        return itemService.GetItemById(itemId)
                .map(itemMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());

    }

    @GetMapping()
    public ResponseEntity<List<ItemResponse>> GetAllItems(){

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

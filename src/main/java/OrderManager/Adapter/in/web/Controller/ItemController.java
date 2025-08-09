package OrderManager.Adapter.in.web;

import OrderManager.Domain.Model.Item;
import OrderManager.Application.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> CreateItem(@RequestBody Item item){
        Optional<Item> result = itemService.SaveItem(item);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Item> UpdateItem(@RequestBody Item item){
        Optional<Item> result = itemService.SaveItem(item);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{Id}")
    public ResponseEntity<Item> GetItemById(@PathVariable("Id") UUID itemId){
        Optional<Item> result = itemService.GetItemById(itemId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/getall")
    public ResponseEntity<List<Item>> GetAllItems(){
        Optional<List<Item>> items = itemService.GetAllItems();
        return items.map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());

    }

    @DeleteMapping("{Id}")
    public ResponseEntity<Boolean> DeleteItem(@PathVariable("Id") UUID itemId){
        boolean isDeleted = itemService.DeleteItem(itemId);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}

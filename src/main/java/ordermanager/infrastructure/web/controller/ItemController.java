package ordermanager.infrastructure.web.controller;

import ordermanager.domain.model.ItemDomain;
import ordermanager.infrastructure.service.ItemService;
import ordermanager.infrastructure.web.dto.item.CreateItemRequest;
import ordermanager.infrastructure.web.dto.item.ItemResponse;
import ordermanager.infrastructure.web.dto.item.UpdateItemRequest;
import ordermanager.infrastructure.mapper.ItemMapper;
import ordermanager.infrastructure.web.exception.ErrorStructureException;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse CreateItem(@Valid @RequestBody CreateItemRequest itemBody){

        ItemDomain item = itemMapper.createDomain(itemBody);
        return itemService.CreateItem(item).map(itemMapper::domainToResponse).getOrElseThrow(ErrorStructureException::new);
    }

    @PutMapping("/{itemId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ItemResponse UpdateItem(@PathVariable UUID itemId, @Valid @RequestBody UpdateItemRequest itemBody){
        ItemDomain item = itemMapper.updateDomain(itemBody);

        return itemService.UpdateItem(itemId,item).map(itemMapper::domainToResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ItemResponse GetItemById(@PathVariable("itemId") UUID itemId){

        return itemService.GetItemById(itemId).map(itemMapper::domainToResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @GetMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ItemResponse> GetAllItems(){

        return itemService.GetAllItems().stream()
                .map(itemMapper::domainToResponse)
                .toList();

    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void DeleteItem(@PathVariable("itemId") UUID itemId){
        itemService.DeleteItem(itemId).getOrElseThrow(ErrorStructureException::new);
    }
}

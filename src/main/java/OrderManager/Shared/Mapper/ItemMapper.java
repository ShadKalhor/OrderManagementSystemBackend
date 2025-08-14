package OrderManager.Shared.Mapper;

import OrderManager.Domain.Model.Item;
import OrderManager.Shared.Dto.ItemDto;
import java.math.BigDecimal;

public class ItemMapper {

    public Item toDomain(ItemDto.CreateItemRequest r) {
        if (r == null) return null;
        Item i = new Item();
        i.setName(r.name());
        i.setDescription(r.description());
        i.setPrice(r.price());
        i.setSize(r.size());
        i.setDiscount(r.discount());
        i.setAvailable(r.isAvailable());
        i.setQuantity(r.quantity());
        return i;
    }

    public void update(Item i, ItemDto.UpdateItemRequest r) {
        if (i == null || r == null) return;
        if (r.name() != null) i.setName(r.name());
        if (r.description() != null) i.setDescription(r.description());
        if (r.price() != null) i.setPrice(r.price());
        if (r.size() != null) i.setSize(r.size());
        if (r.discount() != null) i.setDiscount(r.discount());
        if (r.isAvailable() != null) i.setAvailable(r.isAvailable());
        if (r.quantity() != null) i.setQuantity(r.quantity());
    }

    public ItemDto.ItemResponse toResponse(Item i) {
        if (i == null) return null;
        return new ItemDto.ItemResponse(
            i.getId(), i.getName(), i.getDescription(),
            i.getPrice(), i.getSize(), i.getDiscount(),
            i.isAvailable(), i.getQuantity()
        );
    }

    public ItemDto.ItemSummary toSummary(Item i) {
        if (i == null) return null;
        return new ItemDto.ItemSummary(i.getId(), i.getName(), i.getSize());
    }
}

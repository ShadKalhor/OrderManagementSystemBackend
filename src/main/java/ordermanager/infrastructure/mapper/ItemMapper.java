package ordermanager.infrastructure.mapper;

import ordermanager.domain.dto.item.*;
import ordermanager.domain.dto.orderitem.OrderItemDto;
import ordermanager.infrastructure.store.persistence.entity.OrderItem;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.Item;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE

)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    Item create(CreateItemRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    Item update(UpdateItemRequest r);

    @Mapping(source = "available", target = "isAvailable")
    ItemResponse toResponse(Item entity);

    ItemSummary toSummary(Item entity);

    ItemDto toOrderDto(Item entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "isAvailable", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    Item toEntity(ItemDto ItemDto);

}

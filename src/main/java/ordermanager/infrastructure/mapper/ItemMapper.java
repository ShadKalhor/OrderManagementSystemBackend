package ordermanager.infrastructure.mapper;

import ordermanager.domain.dto.item.CreateItemRequest;
import ordermanager.domain.dto.item.ItemResponse;
import ordermanager.domain.dto.item.ItemSummary;
import ordermanager.domain.dto.item.UpdateItemRequest;
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

    OrderItemDto toOrderDto(OrderItem entity);

    OrderItem toEntity(OrderItemDto orderItemDto);



}

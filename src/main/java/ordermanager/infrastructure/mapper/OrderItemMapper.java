package ordermanager.infrastructure.mapper;

import ordermanager.domain.model.OrderItemDomain;
import ordermanager.infrastructure.store.persistence.entity.Item;
import ordermanager.infrastructure.web.dto.orderitem.CreateOrderItemRequest;
import ordermanager.infrastructure.web.dto.orderitem.OrderItemDto;
import ordermanager.infrastructure.web.dto.orderitem.OrderItemResponse;
import ordermanager.infrastructure.web.dto.orderitem.UpdateOrderItemRequest;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.OrderItem;

import java.util.UUID;

@Mapper(
    componentModel = "spring",
    uses = { ItemMapper.class,OrderMapper.class, UserAddressMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "itemId", target = "item.id")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem create(CreateOrderItemRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "order", ignore = true)
    void update(@MappingTarget OrderItem entity, UpdateOrderItemRequest r);

    OrderItemResponse toResponse(OrderItem entity);


    OrderItemDto toOrderDto(OrderItem entity);


    @Mapping(source = "item", target = "itemId")
    OrderItemDomain toDomain(OrderItem entity);

    @Mapping(target = "order", ignore = true)
    @Mapping(source = "itemId", target = "item")
    OrderItem toEntity(OrderItemDomain domain);


    default UUID map(Item item)    {
        return item == null ? null : item.getId();
    }

    default Item map(UUID id) {
        if (id == null) return null;

        Item item = new Item();
        item.setId(id);
        return item;
    }

}

package ordermanager.infrastructure.mapper;

import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import ordermanager.infrastructure.web.dto.orderitem.CreateOrderItemRequest;
import ordermanager.infrastructure.web.dto.orderitem.OrderItemResponse;
import ordermanager.infrastructure.web.dto.orderitem.UpdateOrderItemRequest;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.OrderItem;

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


}

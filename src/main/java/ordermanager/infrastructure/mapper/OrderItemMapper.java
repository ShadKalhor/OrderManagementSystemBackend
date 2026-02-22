package ordermanager.infrastructure.mapper;

import ordermanager.domain.dto.order.OrderDto;
import ordermanager.domain.dto.orderitem.CreateOrderItemRequest;
import ordermanager.domain.dto.orderitem.OrderItemDto;
import ordermanager.domain.dto.orderitem.OrderItemResponse;
import ordermanager.domain.dto.orderitem.UpdateOrderItemRequest;
import ordermanager.infrastructure.store.persistence.entity.Order;
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


    OrderItemDto toOrderDto(OrderItem entity);

    OrderItem toEntity(OrderItemDto orderItemDto);


}

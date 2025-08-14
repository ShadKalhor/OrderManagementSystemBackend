package OrderManager.Shared.Mapper;

import OrderManager.Shared.Dto.OrderItemDtos.CreateOrderItemRequest;
import OrderManager.Shared.Dto.OrderItemDtos.OrderItemResponse;
import OrderManager.Shared.Dto.OrderItemDtos.UpdateOrderItemRequest;
import org.mapstruct.*;
import OrderManager.Domain.Model.OrderItem;

@Mapper(
    componentModel = "spring",
    uses = { ItemMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "itemId", target = "item.id")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem toDomain(CreateOrderItemRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "order", ignore = true)
    void update(@MappingTarget OrderItem entity, UpdateOrderItemRequest r);

    OrderItemResponse toResponse(OrderItem entity);
}

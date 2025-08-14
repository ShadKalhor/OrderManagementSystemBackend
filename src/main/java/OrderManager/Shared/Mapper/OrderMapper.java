package OrderManager.Shared.Mapper;

import OrderManager.Shared.Dto.OrderDtos.CreateOrderRequest;
import OrderManager.Shared.Dto.OrderDtos.OrderResponse;
import OrderManager.Shared.Dto.OrderDtos.UpdateOrderRequest;
import org.mapstruct.*;
import OrderManager.Domain.Model.Order;

@Mapper(
    componentModel = "spring",
    uses = { UserMapper.class, UserAddressMapper.class, DriverMapper.class, OrderItemMapper.class, MoneyMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId",    target = "user.id")
    @Mapping(source = "addressId", target = "address.id")
    @Mapping(source = "driverId",  target = "driver.id")
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "deliveryFee", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    Order toDomain(CreateOrderRequest r);

    @Mapping(source = "userId",    target = "user.id")
    @Mapping(source = "addressId", target = "address.id")
    @Mapping(source = "driverId",  target = "driver.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "deliveryFee", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    void update(@MappingTarget Order entity, UpdateOrderRequest r);

    @Mapping(source = "deliveryFee", target = "deliveryFee", qualifiedByName = "doubleToBigDecimal")
    OrderResponse toResponse(Order entity);
}

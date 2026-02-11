package OrderManager.Shared.Mapper;

import OrderManager.Domain.Model.Driver;
import OrderManager.Domain.Model.User;
import OrderManager.Domain.Model.UserAddress;
import OrderManager.Shared.Dto.OrderDtos.CreateOrderRequest;
import OrderManager.Shared.Dto.OrderDtos.OrderResponse;
import OrderManager.Shared.Dto.OrderDtos.UpdateOrderRequest;
import org.mapstruct.*;
import OrderManager.Domain.Model.Order;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
    @Mapping(target = "reservation", ignore = true)
    //Make Sure To Map items later!!
    @Mapping(target = "items", ignore = true)
    Order toDomain(CreateOrderRequest r);

    @Mapping(source = "userId",    target = "user.id")
    @Mapping(source = "addressId", target = "address.id")
    @Mapping(source = "driverId",  target = "driver.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "deliveryFee", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    //Make Sure To Map items later!!
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    void update(@MappingTarget Order entity, UpdateOrderRequest r);

    @Mapping(source = "deliveryFee", target = "deliveryFee", qualifiedByName = "doubleToBigDecimal")
    OrderResponse toResponse(Order entity);

    @AfterMapping
    default void fillNestedOnCreate(CreateOrderRequest r, @MappingTarget Order entity) {
        if (r == null) return;
        setIfNotNull(r.userId(), entity::setUser, User::new);
        setIfNotNull(r.addressId(), entity::setAddress, UserAddress::new);
        setIfNotNull(r.driverId(), entity::setDriver, Driver::new);
    }

    @AfterMapping
    default void fillNestedOnUpdate(UpdateOrderRequest r, @MappingTarget Order entity) {
        if (r == null) return;
        setIfNotNull(r.userId(), entity::setUser, User::new);
        setIfNotNull(r.addressId(), entity::setAddress, UserAddress::new);
        setIfNotNull(r.driverId(), entity::setDriver, Driver::new);
    }

    private <T> void setIfNotNull(UUID id, Consumer<T> setter, Supplier<T> creator) {
        if (id != null) {
            T obj = creator.get();
            // Use reflection to call setId(UUID)
            try {
                obj.getClass().getMethod("setId", UUID.class).invoke(obj, id);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to set id on " + obj.getClass(), e);
            }
            setter.accept(obj);
        }
    }
}

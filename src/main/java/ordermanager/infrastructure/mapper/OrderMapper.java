package ordermanager.infrastructure.mapper;

import ordermanager.domain.model.OrderDomain;
import ordermanager.infrastructure.store.persistence.entity.*;
import ordermanager.infrastructure.web.dto.order.CreateOrderRequest;
import ordermanager.infrastructure.web.dto.order.OrderDto;
import ordermanager.infrastructure.web.dto.order.OrderResponse;
import ordermanager.infrastructure.web.dto.order.UpdateOrderRequest;
import org.mapstruct.*;
import org.springframework.jmx.export.annotation.ManagedOperation;

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
    Order create(CreateOrderRequest r);

    @Mapping(target = "user.id", ignore = true)
    @Mapping(source = "addressId", target = "address.id")
    @Mapping(source = "driverId",  target = "driver.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "deliveryFee", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order update(UpdateOrderRequest r);


    OrderResponse toResponse(Order entity);

    OrderDto toOrderDto(Order entity);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "items", target = "itemIds")
    @Mapping(source = "reservation.id", target = "reservationId")
    OrderDomain toDomain(Order entity);


    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "addressId", target = "address.id")
    @Mapping(source = "driverId", target = "driver.id")
    @Mapping(source = "itemIds", target = "items")
    @Mapping(source = "reservationId", target = "reservation.id")
    Order toEntity(OrderDomain domain);



    default UUID map(OrderItem item)    {
        return item == null ? null : item.getId();
    }

    default OrderItem map(UUID id) {
        if (id == null) return null;

        OrderItem item = new OrderItem();
        item.setId(id);
        return item;
    }


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
        setIfNotNull(r.addressId(), entity::setAddress, UserAddress::new);
        setIfNotNull(r.driverId(), entity::setDriver, Driver::new);
    }

    private <T> void setIfNotNull(UUID id, Consumer<T> setter, Supplier<T> creator) {
        if (id != null) {
            T obj = creator.get();

            try {
                obj.getClass().getMethod("setId", UUID.class).invoke(obj, id);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to set id on " + obj.getClass(), e);
            }
            setter.accept(obj);
        }
    }

    @AfterMapping
    default void linkItemsToOrder(@MappingTarget Order entity) {
        if (entity.getItems() == null) return;
        for (var oi : entity.getItems()) {
            oi.setOrder(entity);
        }
    }
}

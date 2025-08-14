package OrderManager.Shared.Mapper;

import OrderManager.Domain.Model.*;
import OrderManager.Domain.Model.Utilities.*;
import OrderManager.Shared.Dto.OrderDto;
import OrderManager.Shared.Dto.UserDto;
import OrderManager.Shared.Dto.UserAddressDto;
import OrderManager.Shared.Dto.DriverDto;
import OrderManager.Shared.Dto.OrderItemDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderMapper {

    private final OrderItemMapper orderItemMapper = new OrderItemMapper();
    private final UserMapper userMapper = new UserMapper();
    private final UserAddressMapper addressMapper = new UserAddressMapper();
    private final DriverMapper driverMapper = new DriverMapper();

    public Order toDomain(OrderDto.CreateOrderRequest r) {
        if (r == null) return null;
        Order o = new Order();
        if (r.userId() != null) {
            User u = new User();
            u.setId(r.userId());
            o.setUser(u);
        }
        if (r.addressId() != null) {
            UserAddress a = new UserAddress();
            a.setId(r.addressId());
            o.setAddress(a);
        }
        if (r.driverId() != null) {
            Driver d = new Driver();
            d.setId(r.driverId());
            o.setDriver(d);
        }
        if (r.status() != null) o.setStatus(r.status());
        if (r.deliveryStatus() != null) o.setDeliveryStatus(r.deliveryStatus());
        if (r.items() != null) {
            List<OrderItem> items = new ArrayList<>();
            for (OrderItemDto.CreateOrderItemRequest itemReq : r.items()) {
                items.add(orderItemMapper.toDomain(itemReq));
            }
            o.setItems(items);
        }
        o.setNotes(r.notes());
        return o;
    }

    public void update(Order o, OrderDto.UpdateOrderRequest r) {
        if (o == null || r == null) return;
        if (r.userId() != null) {
            User u = new User();
            u.setId(r.userId());
            o.setUser(u);
        }
        if (r.addressId() != null) {
            UserAddress a = new UserAddress();
            a.setId(r.addressId());
            o.setAddress(a);
        }
        if (r.driverId() != null) {
            Driver d = new Driver();
            d.setId(r.driverId());
            o.setDriver(d);
        }
        if (r.status() != null) o.setStatus(r.status());
        if (r.deliveryStatus() != null) o.setDeliveryStatus(r.deliveryStatus());
        if (r.items() != null) {
            List<OrderItem> items = new ArrayList<>();
            for (OrderItemDto.CreateOrderItemRequest itemReq : r.items()) {
                items.add(orderItemMapper.toDomain(itemReq));
            }
            o.setItems(items);
        }
        if (r.notes() != null) o.setNotes(r.notes());
    }

    public OrderDto.OrderResponse toResponse(Order o) {
        if (o == null) return null;
        UserDto.UserSummary userSummary = o.getUser() != null ? userMapper.toSummary(o.getUser()) : null;
        UserAddressDto.UserAddressResponse addr = o.getAddress() != null ? addressMapper.toResponse(o.getAddress()) : null;
        DriverDto.DriverResponse driverResp = o.getDriver() != null ? driverMapper.toResponse(o.getDriver()) : null;
        List<OrderItemDto.OrderItemResponse> itemResponses = null;
        if (o.getItems() != null) {
            itemResponses = o.getItems().stream().map(orderItemMapper::toResponse).collect(Collectors.toList());
        }
        return new OrderDto.OrderResponse(
            o.getId(),
            userSummary,
            addr,
            driverResp,
            o.getStatus(),
            o.getDeliveryStatus(),
            itemResponses,
            o.getSubTotal(),
            BigDecimal.valueOf(o.getDeliveryFee()),
            o.getTax(),
            o.getTotalPrice(),
            o.getNotes()
        );
    }
}

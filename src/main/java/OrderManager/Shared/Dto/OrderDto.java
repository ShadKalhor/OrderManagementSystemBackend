package OrderManager.Shared.Dto;

import OrderManager.Domain.Model.Utilities.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private UUID id;
    private UserDto user;
    private UserAddressDto address;
    private DriverDto driver;
    private Status status;
    private DeliveryStatus deliveryStatus;
    private List<OrderItemDto> items;
    private double subTotal;
    private double deliveryFee;
    private double tax;
    private double totalPrice;
    private String notes;
}

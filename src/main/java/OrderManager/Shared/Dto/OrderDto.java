package OrderManager.Shared.Dto;

import OrderManager.Domain.Model.Utilities.*;
import OrderManager.Shared.Validation.EnumOrNull;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    public record CreateOrderRequest(
            @NotNull UUID userId,
            @NotNull UUID addressId,
            UUID driverId,
            @EnumOrNull(value = Status.class)
            Status status,
            @EnumOrNull(value = DeliveryStatus.class)
            DeliveryStatus deliveryStatus,
            @Valid @NotEmpty List<OrderItemDto.CreateOrderItemRequest> items,
            @Size(max=255) String notes
    ) {}

    public record UpdateOrderRequest(
            UUID userId,
            UUID addressId,
            UUID driverId,
            @Valid @NotEmpty List<OrderItemDto.CreateOrderItemRequest> items,
            @EnumOrNull(Status.class)
            Status status,
            @EnumOrNull(DeliveryStatus.class)
            DeliveryStatus deliveryStatus,
            @Size(max=255) String notes
    ) {}

    public record OrderResponse(
            UUID id,
            UserDto.UserSummary user,
            UserAddressDto.UserAddressResponse address,
            DriverDto.DriverResponse driver,
            Status status,
            DeliveryStatus deliveryStatus,
            List<OrderItemDto.OrderItemResponse> items,
            BigDecimal subTotal, BigDecimal deliveryFee, BigDecimal tax, BigDecimal totalPrice,
            String notes
    ) {}

}

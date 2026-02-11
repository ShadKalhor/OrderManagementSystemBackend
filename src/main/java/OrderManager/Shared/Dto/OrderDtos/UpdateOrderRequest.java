package OrderManager.Shared.Dto.OrderDtos;

import OrderManager.Domain.Model.Utilities;
import OrderManager.Shared.Dto.OrderItemDtos.CreateOrderItemRequest;
import OrderManager.Shared.Validation.EnumOrNull;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record UpdateOrderRequest(
        UUID userId,
        UUID addressId,
        UUID driverId,
        @Valid @NotEmpty List<CreateOrderItemRequest> items,
        @EnumOrNull(Utilities.Status.class)
        Utilities.Status status,
        @EnumOrNull(Utilities.DeliveryStatus.class)
        Utilities.DeliveryStatus deliveryStatus,
        @Size(max=255) String notes
) {}


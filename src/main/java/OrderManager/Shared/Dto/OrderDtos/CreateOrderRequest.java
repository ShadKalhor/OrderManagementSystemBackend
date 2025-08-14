package OrderManager.Shared.Dto.OrderDtos;


import OrderManager.Domain.Model.Utilities;
import OrderManager.Shared.Dto.OrderItemDtos.CreateOrderItemRequest;
import OrderManager.Shared.Validation.EnumOrNull;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID userId,
        @NotNull UUID addressId,
        UUID driverId,
        @EnumOrNull(value = Utilities.Status.class)
        Utilities.Status status,
        @EnumOrNull(value = Utilities.DeliveryStatus.class)
        Utilities.DeliveryStatus deliveryStatus,
        @Valid @NotEmpty List<CreateOrderItemRequest> items,
        @Size(max=255) String notes
) {}

package ordermanager.shared.dto.order;


import ordermanager.domain.model.Utilities;
import ordermanager.shared.dto.orderitem.CreateOrderItemRequest;
import ordermanager.shared.validation.EnumOrNull;

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

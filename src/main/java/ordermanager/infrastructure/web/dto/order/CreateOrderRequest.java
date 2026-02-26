package ordermanager.infrastructure.web.dto.order;


import ordermanager.infrastructure.web.dto.orderitem.CreateOrderItemRequest;
import ordermanager.domain.model.DeliveryStatus;
import ordermanager.domain.model.Status;
import ordermanager.infrastructure.validation.EnumOrNull;

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
        @EnumOrNull(value = Status.class)
        Status status,
        @EnumOrNull(value = DeliveryStatus.class)
        DeliveryStatus deliveryStatus,
        @Valid @NotEmpty List<CreateOrderItemRequest> items,
        @Size(max=255) String notes
) {}

package ordermanager.infrastructure.web.dto.order;

import ordermanager.domain.model.DeliveryStatus;
import ordermanager.domain.model.Status;
import ordermanager.infrastructure.validation.EnumOrNull;

import javax.validation.constraints.Size;
import java.util.UUID;

public record UpdateOrderRequest(
        UUID addressId,
        UUID driverId,
        @EnumOrNull(Status.class)
        Status status,
        @EnumOrNull(DeliveryStatus.class)
        DeliveryStatus deliveryStatus,
        @Size(max=255) String notes
) {}


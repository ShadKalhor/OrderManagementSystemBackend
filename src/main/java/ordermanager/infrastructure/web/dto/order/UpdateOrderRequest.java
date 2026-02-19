package ordermanager.infrastructure.web.dto.order;

import ordermanager.infrastructure.validation.EnumOrNull;
import ordermanager.domain.model.Utilities.*;

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


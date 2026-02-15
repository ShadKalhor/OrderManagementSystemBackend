package ordermanager.shared.dto.order;

import ordermanager.domain.model.Utilities;
import ordermanager.shared.validation.EnumOrNull;

import javax.validation.constraints.Size;
import java.util.UUID;

public record UpdateOrderRequest(
        UUID addressId,
        UUID driverId,
        @EnumOrNull(Utilities.Status.class)
        Utilities.Status status,
        @EnumOrNull(Utilities.DeliveryStatus.class)
        Utilities.DeliveryStatus deliveryStatus,
        @Size(max=255) String notes
) {}


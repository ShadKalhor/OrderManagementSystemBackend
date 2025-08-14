package OrderManager.Shared.Dto.OrderItemDtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderItemRequest(
        @NotNull UUID itemId,
        @Min(1) int quantity
) {}


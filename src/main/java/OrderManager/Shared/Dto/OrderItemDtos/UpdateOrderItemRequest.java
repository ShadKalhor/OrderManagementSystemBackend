package OrderManager.Shared.Dto.OrderItemDtos;


import javax.validation.constraints.Min;

public record UpdateOrderItemRequest(
        @Min(1) Integer quantity
) {}

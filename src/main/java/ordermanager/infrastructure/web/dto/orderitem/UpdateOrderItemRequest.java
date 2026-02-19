package ordermanager.infrastructure.web.dto.orderitem;


import javax.validation.constraints.Min;

public record UpdateOrderItemRequest(
        @Min(1) Integer quantity
) {}

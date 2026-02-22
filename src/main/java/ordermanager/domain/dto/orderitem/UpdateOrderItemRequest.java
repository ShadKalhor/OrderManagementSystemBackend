package ordermanager.domain.dto.orderitem;


import javax.validation.constraints.Min;

public record UpdateOrderItemRequest(
        @Min(1) Integer quantity
) {}

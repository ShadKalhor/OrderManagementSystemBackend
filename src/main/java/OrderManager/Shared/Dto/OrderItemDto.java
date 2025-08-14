package OrderManager.Shared.Dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

    private UUID id;

    @Valid
    @NotNull
    private ItemDto item;

    @NotNull
    @Min(value = 1, message = "Must Acquire 1 or More of Said Item")
    private int quantity;

    private double totalPrice;

    public record CreateOrderItemRequest(
            @NotNull UUID itemId,
            @Min(1) int quantity
    ) {}

    public record UpdateOrderItemRequest(
            @Min(1) Integer quantity
    ) {}

    public record OrderItemResponse(
            UUID id,
            ItemDto.ItemSummary item,
            int quantity,
            BigDecimal totalPrice
    ) {}
}

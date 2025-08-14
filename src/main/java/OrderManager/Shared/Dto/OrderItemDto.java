package OrderManager.Shared.Dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private UUID id;
    private ItemDto item;
    private int quantity;
    private double totalPrice;
}

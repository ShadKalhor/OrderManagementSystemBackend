package OrderManager.Shared.Dto;


import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {

    public record CreateItemRequest(
            @NotBlank String name,
            @Size(max=255) String description,
            @DecimalMin(value="0.0", inclusive=true) BigDecimal price,
            @NotBlank String size,
            @DecimalMin(value="0.0", inclusive=true) BigDecimal discount,
            boolean isAvailable,
            @Min(0) int quantity
    ) {}

    public record UpdateItemRequest(
            String name,
            String description,
            @DecimalMin(value="0.0", inclusive=true) BigDecimal price,
            String size,
            @DecimalMin(value = "0.0", inclusive = true, message = "Discount cannot be negative")
            @DecimalMax(value = "1.0", inclusive = true, message = "Discount cannot be greater than 1.0")
            BigDecimal discount,
            Boolean isAvailable,
            Integer quantity
    ) {}

    public record ItemSummary(UUID id, String name, String size) {}
    public record ItemResponse(
            UUID id, String name, String description,
            BigDecimal price, String size, BigDecimal discount,
            boolean isAvailable, int quantity
    ) {}
}


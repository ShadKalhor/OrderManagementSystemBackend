package OrderManager.Shared.Dto.ItemDtos;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

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

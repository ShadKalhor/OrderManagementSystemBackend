package ordermanager.shared.dto.item;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateItemRequest(
        @NotBlank String name,
        @Size(max=255) String description,
        @DecimalMin(value="0.0", inclusive=true) BigDecimal price,
        @NotBlank String size,
        @DecimalMin(value="0.0", inclusive=true) BigDecimal discount,
        boolean isAvailable,
        @Min(0) int quantity
) {}

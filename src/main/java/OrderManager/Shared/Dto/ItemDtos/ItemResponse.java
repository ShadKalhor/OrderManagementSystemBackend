package OrderManager.Shared.Dto.ItemDtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemResponse(
        UUID id, String name, String description,
        BigDecimal price, String size, BigDecimal discount,
        boolean isAvailable, int quantity
) {}

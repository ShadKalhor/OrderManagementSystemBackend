package OrderManager.Shared.Dto.OrderItemDtos;


import OrderManager.Shared.Dto.ItemDtos.ItemSummary;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        ItemSummary item,
        int quantity,
        BigDecimal totalPrice
) {}

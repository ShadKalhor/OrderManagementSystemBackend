package ordermanager.infrastructure.web.dto.orderitem;


import ordermanager.infrastructure.web.dto.item.ItemSummary;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        ItemSummary item,
        int quantity,
        BigDecimal totalPrice
) {}

package ordermanager.domain.dto.orderitem;


import ordermanager.domain.dto.item.ItemSummary;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        ItemSummary item,
        int quantity,
        BigDecimal totalPrice
) {}

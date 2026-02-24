package ordermanager.infrastructure.web.dto.orderitem;

import ordermanager.infrastructure.web.dto.item.ItemDto;

import java.math.BigDecimal;

public record OrderItemDto (
        ItemDto item,
        int quantity,
        BigDecimal totalPrice
){ }

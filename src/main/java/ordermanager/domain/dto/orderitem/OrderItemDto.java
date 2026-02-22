package ordermanager.domain.dto.orderitem;

import ordermanager.domain.dto.item.ItemDto;

import java.util.List;

public record OrderItemDto (
        ItemDto item,
        int quantity
){ }

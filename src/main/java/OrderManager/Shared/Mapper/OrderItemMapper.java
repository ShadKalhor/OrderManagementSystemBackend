package OrderManager.Shared.Mapper;

import OrderManager.Domain.Model.Item;
import OrderManager.Domain.Model.OrderItem;
import OrderManager.Shared.Dto.OrderItemDto;
import OrderManager.Shared.Dto.ItemDto;
import java.math.BigDecimal;

public class OrderItemMapper {

    public OrderItem toDomain(OrderItemDto.CreateOrderItemRequest r) {
        if (r == null) return null;
        OrderItem oi = new OrderItem();
        if (r.itemId() != null) {
            Item item = new Item();
            item.setId(r.itemId());
            oi.setItem(item);
        }
        oi.setQuantity(r.quantity());
        // totalPrice should be calculated in service using item price & discounts
        return oi;
    }

    public void update(OrderItem oi, OrderItemDto.UpdateOrderItemRequest r) {
        if (oi == null || r == null) return;
        if (r.quantity() != null) oi.setQuantity(r.quantity());
    }

    public OrderItemDto.OrderItemResponse toResponse(OrderItem oi) {
        if (oi == null) return null;
        ItemDto.ItemSummary itemSummary = null;
        if (oi.getItem() != null) {
            itemSummary = new ItemDto.ItemSummary(oi.getItem().getId(), oi.getItem().getName(), oi.getItem().getSize());
        }
        return new OrderItemDto.OrderItemResponse(
            oi.getId(),
            itemSummary,
            oi.getQuantity(),
            oi.getTotalPrice()
        );
    }
}

package ordermanager.domain.dto.order;

import ordermanager.domain.dto.orderitem.OrderItemDto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto (
        List<OrderItemDto> items,
        BigDecimal subTotal,
        BigDecimal deliveryFee,
        BigDecimal tax,
        BigDecimal totalPrice){
}

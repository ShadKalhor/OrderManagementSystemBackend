package ordermanager.infrastructure.web.dto.order;

import ordermanager.infrastructure.web.dto.orderitem.OrderItemDto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto (
        List<OrderItemDto> orderItems,
        BigDecimal subTotal,
        BigDecimal deliveryFee,
        BigDecimal tax,
        BigDecimal totalPrice){
}

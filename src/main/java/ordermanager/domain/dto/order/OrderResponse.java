package ordermanager.domain.dto.order;


import ordermanager.domain.dto.driver.DriverResponse;
import ordermanager.domain.dto.orderitem.OrderItemResponse;
import ordermanager.domain.dto.useraddress.UserAddressResponse;
import ordermanager.domain.dto.user.UserSummary;
import ordermanager.infrastructure.store.persistence.entity.Utilities.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UserSummary user,
        UserAddressResponse address,
        DriverResponse driver,
        Status status,
        DeliveryStatus deliveryStatus,
        List<OrderItemResponse> items,
        BigDecimal subTotal,
        BigDecimal deliveryFee,
        BigDecimal tax,
        BigDecimal totalPrice,
        String notes
) {}

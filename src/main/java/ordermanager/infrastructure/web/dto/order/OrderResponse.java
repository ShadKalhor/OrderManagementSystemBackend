package ordermanager.infrastructure.web.dto.order;


import ordermanager.infrastructure.web.dto.driver.DriverResponse;
import ordermanager.infrastructure.web.dto.orderitem.OrderItemResponse;
import ordermanager.infrastructure.web.dto.useraddress.UserAddressResponse;
import ordermanager.infrastructure.web.dto.user.UserSummary;
import ordermanager.infrastructure.store.persistence.entity.DeliveryStatus;
import ordermanager.infrastructure.store.persistence.entity.Status;

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

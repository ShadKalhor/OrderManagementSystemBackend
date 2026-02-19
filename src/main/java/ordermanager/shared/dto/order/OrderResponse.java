package ordermanager.shared.dto.order;


import ordermanager.shared.dto.driver.DriverResponse;
import ordermanager.shared.dto.orderitem.OrderItemResponse;
import ordermanager.shared.dto.useraddress.UserAddressResponse;
import ordermanager.shared.dto.user.UserSummary;
import ordermanager.domain.model.Utilities.*;

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

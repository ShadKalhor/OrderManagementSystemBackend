package ordermanager.shared.dto.order;


import ordermanager.domain.model.Utilities;
import ordermanager.shared.dto.driver.DriverResponse;
import ordermanager.shared.dto.orderitem.OrderItemResponse;
import ordermanager.shared.dto.useraddress.UserAddressResponse;
import ordermanager.shared.dto.user.UserSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UserSummary user,
        UserAddressResponse address,
        DriverResponse driver,
        Utilities.Status status,
        Utilities.DeliveryStatus deliveryStatus,
        List<OrderItemResponse> items,
        BigDecimal subTotal,
        BigDecimal deliveryFee,
        BigDecimal tax,
        BigDecimal totalPrice,
        String notes
) {}

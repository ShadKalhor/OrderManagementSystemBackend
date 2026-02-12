package OrderManager.Shared.Dto.OrderDtos;


import OrderManager.Domain.Model.Utilities;
import OrderManager.Shared.Dto.DriverDtos.DriverResponse;
import OrderManager.Shared.Dto.OrderItemDtos.OrderItemResponse;
import OrderManager.Shared.Dto.UserAddressDtos.UserAddressResponse;
import OrderManager.Shared.Dto.UserDtos.UserSummary;

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

package OrderManager.Shared.Dto.DriverDtos;

import OrderManager.Shared.Dto.UserDtos.UserResponse;

import java.util.UUID;

public record DriverResponse(
        UUID id,
        String name,
        String vehicleNumber,
        int age,
        UserResponse account
) {}

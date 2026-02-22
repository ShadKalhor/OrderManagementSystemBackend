package ordermanager.domain.dto.driver;

import ordermanager.domain.dto.user.UserResponse;

import java.util.UUID;

public record DriverResponse(
        UUID id,
        String name,
        String vehicleNumber,
        int age,
        UserResponse account
) {}

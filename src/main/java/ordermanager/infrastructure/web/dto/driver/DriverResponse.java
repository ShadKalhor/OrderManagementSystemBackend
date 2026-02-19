package ordermanager.infrastructure.web.dto.driver;

import ordermanager.infrastructure.web.dto.user.UserResponse;

import java.util.UUID;

public record DriverResponse(
        UUID id,
        String name,
        String vehicleNumber,
        int age,
        UserResponse account
) {}

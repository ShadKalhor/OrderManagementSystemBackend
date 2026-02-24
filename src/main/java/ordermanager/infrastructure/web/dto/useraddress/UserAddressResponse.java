package ordermanager.infrastructure.web.dto.useraddress;

import java.util.UUID;

public record UserAddressResponse(
        UUID id,
        UUID userId,
        String name, String city, String description, String type, String street, String residentialNo,
        boolean isPrimary
) {}


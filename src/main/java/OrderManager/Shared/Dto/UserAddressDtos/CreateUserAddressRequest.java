package OrderManager.Shared.Dto.UserAddressDtos;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public record CreateUserAddressRequest(
        @NotNull UUID userId,
        @NotBlank String name,
        @NotBlank String city,
        @Size(max=255) String description,
        @NotBlank String type,
        @NotBlank String street,
        @NotBlank String residentialNo,
        boolean isPrimary
) {}

package OrderManager.Shared.Dto.DriverDtos;

import OrderManager.Shared.Dto.UserDtos.CreateUserRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public record CreateDriverRequest(
        @NotBlank @Size(max = 60) String name,
        @NotBlank String vehicleNumber,
        @Min(value = 18, message = "Cant be Younger Than 18 Years of Age.") int age,
        @Valid @NotNull(message = "Account is required") UUID accountId
) {}

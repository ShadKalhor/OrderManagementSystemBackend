package OrderManager.Shared.Dto;


import OrderManager.Shared.Validation.ZeroOrAdult;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDto {

    public record CreateDriverRequest(
            @NotBlank @Size(max = 60) String name,
            @NotBlank String vehicleNumber,
            @Min(value = 18, message = "Cant be Younger Than 18 Years of Age.") int age,
            @Valid @NotNull(message = "Account is required") UserDto account
    ) {}

    public record UpdateDriverRequest(
            @Size(max = 60) String name,
            String vehicleNumber,
            @ZeroOrAdult(message = "The Driver Must Be 18 or older, or 0 to keep it the same age") Integer age,
            @Valid UserDto account // optional; validated if present
    ) {}

    public record DriverResponse(
            UUID id,
            String name,
            String vehicleNumber,
            int age,
            UserDto account
    ) {}
}
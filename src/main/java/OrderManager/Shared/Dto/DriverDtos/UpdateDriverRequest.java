package OrderManager.Shared.Dto.DriverDtos;

import OrderManager.Shared.Validation.ZeroOrAdult;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.UUID;

public record UpdateDriverRequest(
        @Size(max = 60) String name,
        String vehicleNumber,
        @ZeroOrAdult(message = "The Driver Must Be 18 or older, or 0 to keep it the same age") Integer age,
        UUID accountId
) {}

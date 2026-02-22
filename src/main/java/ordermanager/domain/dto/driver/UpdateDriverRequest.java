package ordermanager.domain.dto.driver;

import ordermanager.infrastructure.validation.ZeroOrAdult;

import javax.validation.constraints.Size;
import java.util.UUID;

public record UpdateDriverRequest(
        @Size(max = 60) String name,
        String vehicleNumber,
        @ZeroOrAdult(message = "The Driver Must Be 18 or older, or 0 to keep it the same age") Integer age,
        UUID accountId
) {}

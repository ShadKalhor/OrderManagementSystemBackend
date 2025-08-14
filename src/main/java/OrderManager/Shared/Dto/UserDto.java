package OrderManager.Shared.Dto;

import OrderManager.Domain.Model.Utilities.*;
import OrderManager.Shared.Validation.Password;
import OrderManager.Shared.Validation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    public record CreateUserRequest(
            @NotBlank @Size(max=60) String name,
            @NotBlank @PhoneNumber String phone,
            @NotBlank @Password(message = "Password Must be Adleast 8 Characters, With Upper, Lower, Digit, and Special Characters.") String password,
            Genders gender,
            UserRoles role
    ) {}

    public record UpdateUserRequest(
            @Size(max=60) String name,
            @PhoneNumber(message = "Phone Number Is Not Valid") String phone,
            @Password(message = "Password Must be Adleast 8 Characters, With Upper, Lower, Digit, and Special Characters.") String password,
            Genders gender,
            UserRoles role
    ) {}
    public record UserSummary(UUID id, String name, String phone, UserRoles role, Genders gender) {}

    public record UserResponse(UUID id, String name, String phone, UserRoles role, Genders gender) {}

}

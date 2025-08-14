package OrderManager.Shared.Dto.UserDtos;

import OrderManager.Domain.Model.Utilities;
import OrderManager.Shared.Validation.Password;
import OrderManager.Shared.Validation.PhoneNumber;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max=60) String name,
        @NotBlank @PhoneNumber String phone,
        @NotBlank @Password(message = "Password Must be Adleast 8 Characters, With Upper, Lower, Digit, and Special Characters.") String password,
        Utilities.Genders gender,
        Utilities.UserRoles role
) {}

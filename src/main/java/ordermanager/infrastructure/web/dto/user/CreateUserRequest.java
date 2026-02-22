package ordermanager.infrastructure.web.dto.user;

import ordermanager.infrastructure.validation.Password;
import ordermanager.infrastructure.validation.PhoneNumber;
import ordermanager.infrastructure.store.persistence.entity.Utilities.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max=60) String name,
        @NotBlank @PhoneNumber String phone,
        @NotBlank @Password(message = "Password Must be Adleast 8 Characters, With Upper, Lower, Digit, and Special Characters.") String password,
        Genders gender,
        UserRoles role
) {}

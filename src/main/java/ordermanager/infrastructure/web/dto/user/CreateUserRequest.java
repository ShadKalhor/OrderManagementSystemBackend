package ordermanager.infrastructure.web.dto.user;

import ordermanager.infrastructure.store.persistence.entity.Genders;
import ordermanager.infrastructure.store.persistence.entity.UserRoles;
import ordermanager.infrastructure.validation.Password;
import ordermanager.infrastructure.validation.PhoneNumber;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max=60) String name,
        @NotBlank @PhoneNumber String phone,
        @NotBlank @Password(message = "Password Must be Adleast 8 Characters, With Upper, Lower, Digit, and Special Characters.") String password,
        Genders gender,
        UserRoles role
) {}

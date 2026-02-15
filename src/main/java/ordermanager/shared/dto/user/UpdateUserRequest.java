package ordermanager.shared.dto.user;

import ordermanager.domain.model.Utilities;
import ordermanager.shared.validation.Password;
import ordermanager.shared.validation.PhoneNumber;

import javax.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max=60) String name,
        @PhoneNumber(message = "Phone Number Is Not Valid") String phone,
        @Password(message = "Password Must be Adleast 8 Characters, With Upper, Lower, Digit, and Special Characters.") String password,
        Utilities.Genders gender,
        Utilities.UserRoles role
) {}

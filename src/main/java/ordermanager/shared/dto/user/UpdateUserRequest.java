package ordermanager.shared.dto.user;

import ordermanager.shared.validation.Password;
import ordermanager.shared.validation.PhoneNumber;
import ordermanager.domain.model.Utilities.*;

import javax.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max=60) String name,
        @PhoneNumber(message = "Phone Number Is Not Valid") String phone,
        @Password(message = "Password Must be Adleast 8 Characters, With Upper, Lower, Digit, and Special Characters.") String password,
        Genders gender,
        UserRoles role
) {}

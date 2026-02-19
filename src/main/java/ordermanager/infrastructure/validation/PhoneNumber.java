package ordermanager.infrastructure.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {
    String message() default "Phone number must start with 07 and be exactly 11 digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

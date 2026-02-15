package ordermanager.shared.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumOrNullValidator.class, EnumOrNullEnumValidator.class})
@Documented
public @interface EnumOrNull {
    String message() default "Value must match one of the allowed enum constants";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> value();
    boolean ignoreCase() default true;
}

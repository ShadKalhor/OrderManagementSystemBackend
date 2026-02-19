package ordermanager.infrastructure.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumOrNullEnumValidator implements ConstraintValidator<EnumOrNull, Enum<?>> {

    private Set<String> allowed;
    private boolean ignoreCase;
    private String allowedForMessage;

    @Override
    public void initialize(EnumOrNull anno) {
        ignoreCase = anno.ignoreCase();
        var constants = anno.value().getEnumConstants();
        if (ignoreCase) {
            allowed = Arrays.stream(constants)
                    .map(e -> e.name().toLowerCase())
                    .collect(Collectors.toSet());
            allowedForMessage = Arrays.stream(constants)
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
        } else {
            allowed = Arrays.stream(constants)
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            allowedForMessage = String.join(", ", allowed);
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext ctx) {
        if (value == null) return true;

        String probe = ignoreCase ? value.name().toLowerCase() : value.name();
        boolean ok = allowed.contains(probe);
        if (!ok) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Invalid value. Allowed: " + allowedForMessage
            ).addConstraintViolation();
        }
        return ok;
    }
}

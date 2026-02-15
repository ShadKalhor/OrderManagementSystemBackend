package ordermanager.shared.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumOrNullValidator implements ConstraintValidator<EnumOrNull, String> {

    private Set<String> allowed;
    private boolean ignoreCase;
    private String allowedForMessage;

    @Override
    public void initialize(EnumOrNull anno) {
        ignoreCase = anno.ignoreCase();
        var constants = anno.value().getEnumConstants();
        if (ignoreCase) {
            allowed = Arrays.stream(constants).map(e -> e.name().toLowerCase()).collect(Collectors.toSet());
            allowedForMessage = Arrays.stream(constants).map(e -> e.name()).collect(Collectors.joining(", "));
        } else {
            allowed = Arrays.stream(constants).map(Enum::name).collect(Collectors.toSet());
            allowedForMessage = String.join(", ", allowed);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null || value.isBlank()) return true;
        String probe = ignoreCase ? value.trim().toLowerCase() : value.trim();
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

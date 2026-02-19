package ordermanager.infrastructure.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZeroOrAdultValidator implements ConstraintValidator<ZeroOrAdult, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext ctx) {
        if (value == null) return true;
        return value == 0 || value >= 18;
    }
}

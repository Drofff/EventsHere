package annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ActualDateValidator implements ConstraintValidator<ActualDate, LocalDateTime> {


    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxDate = now.plus(1, ChronoUnit.YEARS);

        return value.isAfter(now) && (value.isBefore(maxDate) || value.isEqual(maxDate));

    }

    @Override
    public void initialize(ActualDate constraintAnnotation) {

    }
}

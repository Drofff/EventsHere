package annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActualDateValidator.class)
@Documented
public @interface ActualDate {

    String message() default "Wrong date semantics";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

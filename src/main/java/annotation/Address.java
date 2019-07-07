package annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AddressValidator.class)
@Documented
public @interface Address {

    String message () default "Please, specify valid address";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};

}

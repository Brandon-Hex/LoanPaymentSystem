package com.bancx.application.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxTwoDecimalPlacesValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxTwoDecimalPlaces {
    String message() default "Value cannot have more than 2 decimal places";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

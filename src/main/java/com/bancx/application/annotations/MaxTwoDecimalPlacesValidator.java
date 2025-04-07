package com.bancx.application.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class MaxTwoDecimalPlacesValidator implements ConstraintValidator<MaxTwoDecimalPlaces, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) return true; // null handled by @NotNull if needed

        return value.scale() <= 2;
    }
}

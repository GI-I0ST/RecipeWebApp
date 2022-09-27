package com.ghost.recipewebapp.validator;

import com.ghost.recipewebapp.validator.annotation.DurationLimit;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class DurationLimitValidator
        implements ConstraintValidator<DurationLimit, Object> {

    private String secondsField;
    private String minutesField;
    private String hoursField;
    private String daysField;
    private long max;
    private long min;
    private String measureMin;
    private String measureMax;
    private String message;

    public void initialize(DurationLimit constraintAnnotation) {
        this.secondsField = constraintAnnotation.secondsField();
        this.minutesField = constraintAnnotation.minutesField();
        this.hoursField = constraintAnnotation.hoursField();
        this.daysField = constraintAnnotation.daysField();
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
        this.measureMin = constraintAnnotation.measureMin();
        this.measureMax = constraintAnnotation.measureMax();
        this.message = constraintAnnotation.message();
    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        Duration time = Duration.ZERO;

        context.disableDefaultConstraintViolation();
        for (String field : List.of(secondsField, minutesField, hoursField, daysField)) {
            if (!field.isBlank()) {
                // append error to field
                context.buildConstraintViolationWithTemplate(message).addPropertyNode(field).addConstraintViolation();
                // get value from field
                Optional<Object> optional = Optional.ofNullable(new BeanWrapperImpl(value).getPropertyValue(field));
                long fieldValue = optional.map(o -> Long.parseLong(String.valueOf(o))).orElse(0L);
                // increase time
                time = time.plusMinutes(fieldValue);
            }
        }

        if (min >= 0) {
            switch (measureMin) {
                case DurationLimit.SECONDS:
                    return time.toSeconds() >= min;
                case DurationLimit.MINUTES:
                    return time.toMinutes() >= min;
                case DurationLimit.HOURS:
                    return time.toHours() >= min;
                case DurationLimit.DAYS:
                    return time.toDays() >= min;
            }
        }

        if (max >= 0) {
            switch (measureMax) {
                case DurationLimit.SECONDS:
                    return time.toSeconds() <= max;
                case DurationLimit.MINUTES:
                    return time.toMinutes() <= max;
                case DurationLimit.HOURS:
                    return time.toHours() <= max;
                case DurationLimit.DAYS:
                    return time.toDays() <= max;
            }
        }

        return true;
    }
}
package com.ghost.recipewebapp.validator.annotation;

import com.ghost.recipewebapp.validator.DurationLimitValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationLimitValidator.class)
public @interface DurationLimit {

    String SECONDS = "seconds";
    String MINUTES = "minutes";
    String HOURS = "hours";
    String DAYS = "days";

    String message() default "Time is not in limit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String secondsField() default "";

    String minutesField() default "";

    String hoursField() default "";

    String daysField() default "";

    long max() default -1;

    long min() default -1;

    String measureMin() default HOURS;

    String measureMax() default HOURS;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
}
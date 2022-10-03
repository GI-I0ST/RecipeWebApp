package com.ghost.recipewebapp.dto;

import com.ghost.recipewebapp.validator.annotation.DurationLimit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@DurationLimit(minutesField = "minutes", measureMin = DurationLimit.MINUTES, min = 1,
        hoursField = "hours", measureMax = DurationLimit.DAYS, max = 7)
public class RecipeDto {
    protected Long id;

    // image name
    protected String image;

    @Size(min = 5, message = "Title must contain at least 5 symbols")
    @NotBlank(message = "Title is required")
    protected String title;

    @Min(value = 0, message = "Hours must be positive")
    protected int hours;

    @Min(value = 0, message = "Minutes must be positive")
    @Max(value = 59, message = "Minutes must be less than 60")
    protected int minutes;

    // calories in kcal
    @Min(value = 0, message = "Calories must be positive")
    @Max(value = 2_000_000, message = "Calories should be less than 2 000 000")
    protected int calories;

    protected String author;

    protected Boolean favourite;
}

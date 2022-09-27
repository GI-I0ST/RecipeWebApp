package com.ghost.recipewebapp.dto;

import com.ghost.recipewebapp.entity.*;
import com.ghost.recipewebapp.validator.annotation.DurationLimit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@DurationLimit(minutesField = "minutes", measureMin = DurationLimit.MINUTES, min = 1,
        hoursField = "hours", measureMax = DurationLimit.DAYS, max = 7)
public class RecipeDto implements MultipartImageEntity {
    private Long id;

    // image name
    private String image;

    private MultipartFile imageMultipart;

    @Size(min = 5, message = "Title must contain at least 5 symbols")
    @NotBlank(message = "Title is required")
    private String title;

    @Min(value = 0, message = "Hours must be positive")
    private int hours;

    @Min(value = 0, message = "Minutes must be positive")
    @Max(value = 59, message = "Minutes must be less than 60")
    private int minutes;

    // calories in kcal
    @Min(value = 0, message = "Calories must be positive")
    private int calories;

    @Size(min = 1, message = "Recipe must contain at least 1 ingredient")
    @Valid
    private List<Ingredient> ingredientsList = new ArrayList<>();

    //steps to cook
    @Size(min = 1, message = "Recipe must contain at least 1 step")
    @Valid
    private List<Step> stepsList = new ArrayList<>();

    private String author;

    private Boolean favourite;
}

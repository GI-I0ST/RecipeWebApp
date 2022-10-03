package com.ghost.recipewebapp.dto;

import com.ghost.recipewebapp.entity.Ingredient;
import com.ghost.recipewebapp.entity.MultipartImageEntity;
import com.ghost.recipewebapp.entity.Step;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeFullDto extends RecipeDto implements MultipartImageEntity {

    protected MultipartFile imageMultipart;

    @Size(min = 1, message = "Recipe must contain at least 1 ingredient")
    @Valid
    private List<Ingredient> ingredientsList = new ArrayList<>();

    //steps to cook
    @Size(min = 1, message = "Recipe must contain at least 1 step")
    @Valid
    private List<Step> stepsList = new ArrayList<>();
}

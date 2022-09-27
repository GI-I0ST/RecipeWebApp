package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.dto.RecipeDto;
import com.ghost.recipewebapp.dto.RecipeSearch;
import org.springframework.data.domain.Page;

public interface RecipeService {
    RecipeDto getRecipeById(Long id);

    Page<RecipeDto> getRecipesPage(RecipeSearch recipeSearch);

    Long addNewRecipe(RecipeDto newRecipe);

    void editRecipe(RecipeDto newRecipe);

    void deleteRecipeById(Long id);
}

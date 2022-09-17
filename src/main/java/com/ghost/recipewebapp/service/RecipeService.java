package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.RecipeSearch;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Recipe getRecipeById(Long id);

    Page<Recipe> getRecipesPage(RecipeSearch recipeSearch);

    Long addNewRecipe(Recipe newRecipe);

    void editRecipe(Recipe newRecipe);

    void deleteRecipeById(Long id);
}

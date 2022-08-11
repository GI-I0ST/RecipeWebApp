package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.entity.Recipe;

public interface RecipeService {
    Recipe getRecipeById(Long id);

    Long addNewRecipe(Recipe newRecipe);

    void editRecipe(Recipe newRecipe);

    void deleteRecipeById(Long id);
}

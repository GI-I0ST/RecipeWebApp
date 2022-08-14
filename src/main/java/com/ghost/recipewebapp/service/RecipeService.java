package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.entity.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe getRecipeById(Long id);

    List<Recipe> getRecipes();

    Long addNewRecipe(Recipe newRecipe);

    void editRecipe(Recipe newRecipe);

    void deleteRecipeById(Long id);
}

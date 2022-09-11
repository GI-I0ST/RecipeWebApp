package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.entity.Recipe;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Recipe getRecipeById(Long id);

    Page<Recipe> getRecipesPage(int currentPage, int pageSize);

    Long addNewRecipe(Recipe newRecipe);

    void editRecipe(Recipe newRecipe);

    void deleteRecipeById(Long id);
}

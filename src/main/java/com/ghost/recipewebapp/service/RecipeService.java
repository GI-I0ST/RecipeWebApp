package com.ghost.recipewebapp.service;

import com.ghost.recipewebapp.dto.RecipeDto;
import com.ghost.recipewebapp.dto.RecipeFullDto;
import com.ghost.recipewebapp.dto.RecipeSearch;
import org.springframework.data.domain.Page;

public interface RecipeService {
    RecipeFullDto getRecipeById(Long id);

    Page<RecipeDto> getRecipesPage(RecipeSearch recipeSearch);

    Long addNewRecipe(RecipeFullDto newRecipe);

    void editRecipe(RecipeFullDto newRecipe);

    void deleteRecipeById(Long id);

    void addToFavourites(Long recipeId);

    void removeFromFavourites(Long recipeId);
}

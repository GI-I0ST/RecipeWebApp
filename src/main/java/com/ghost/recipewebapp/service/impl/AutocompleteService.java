package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.repository.AutocompleteIngredientRepository;
import com.ghost.recipewebapp.repository.AutocompleteIngredientRepositoryImpl;
import com.ghost.recipewebapp.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AutocompleteService {
    private final AutocompleteIngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public AutocompleteService(AutocompleteIngredientRepositoryImpl ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    // returns Set of Ingredient.product that contains param string
    public List<String> getProductsContainsStr(String input) {
        return ingredientRepository.findProductsContainsStr(input);
    }

    // returns map of recipe title and id where title contains param string
    public List<Map<String, String>> getTitlesContainsStr(String input) {
        List<Recipe> recipeList = recipeRepository.findFirst10ByTitleContainingIgnoreCase(input);

        List<Map<String, String>> result = new ArrayList<>();
        for (Recipe recipe: recipeList) {
            result.add(Map.of(
                    "label", recipe.getTitle(),
                    "value", recipe.getId().toString()
            ));
        }

        return result;
    }
}

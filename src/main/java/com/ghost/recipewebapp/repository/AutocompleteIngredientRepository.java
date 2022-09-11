package com.ghost.recipewebapp.repository;

import java.util.List;


public interface AutocompleteIngredientRepository {
    List<String> findProductsContainsStr(String input);
}

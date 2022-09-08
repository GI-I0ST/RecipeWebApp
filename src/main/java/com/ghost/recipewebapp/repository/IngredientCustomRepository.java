package com.ghost.recipewebapp.repository;

import java.util.List;


public interface IngredientCustomRepository {
    List<String> findProductsContainsStr(String input);
}

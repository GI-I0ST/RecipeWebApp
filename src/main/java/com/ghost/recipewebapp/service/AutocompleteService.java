package com.ghost.recipewebapp.service;

import java.util.List;
import java.util.Map;

public interface AutocompleteService {
    // returns Ingredient.product that contains param string
    List<String> getProductsContainsStr(String input);

    // returns "label" as recipe title and "value" as recipe id where title contains param string
    List<Map<String, String>> getTitlesContainsStr(String input);
}

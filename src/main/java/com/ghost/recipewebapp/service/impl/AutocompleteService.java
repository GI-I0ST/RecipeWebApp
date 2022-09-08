package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.repository.IngredientCustomRepository;
import com.ghost.recipewebapp.repository.IngredientCustomRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutocompleteService {
    private final IngredientCustomRepository ingredientRepository;

    @Autowired
    public AutocompleteService(IngredientCustomRepositoryImpl ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    // returns Set of Ingredient.product that contains param string
    public List<String> getProductsContainsStr(String input){
        return ingredientRepository.findProductsContainsStr(input);
    }
}

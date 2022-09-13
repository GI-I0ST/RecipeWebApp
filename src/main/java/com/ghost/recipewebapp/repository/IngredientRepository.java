package com.ghost.recipewebapp.repository;

import com.ghost.recipewebapp.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, AutocompleteIngredientRepository {
}
package com.ghost.recipewebapp.repository;

import com.ghost.recipewebapp.entity.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
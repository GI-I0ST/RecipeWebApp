package com.ghost.recipewebapp.repository;

import com.ghost.recipewebapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findFirst10ByTitleContainingIgnoreCase(String input);
}
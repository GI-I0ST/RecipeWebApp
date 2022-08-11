package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.repository.RecipeRepository;
import com.ghost.recipewebapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Long addNewRecipe(Recipe newRecipe) {
        //may be improved
        //newRecipe.setUser(testUser);
        newRecipe.getStepsList().forEach(step -> {
            step.setRecipe(newRecipe);
        });

        recipeRepository.save(newRecipe);

        return newRecipe.getId();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return recipe;
    }

    @Override
    public void editRecipe(Recipe newRecipe) {
        Recipe oldRecipe = recipeRepository.findById(newRecipe.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //may be improved
        //newRecipe.setUser(oldRecipe.getUser());

        newRecipe.setCommentsList(oldRecipe.getCommentsList());
        newRecipe.getStepsList().forEach(step -> step.setRecipe(newRecipe));

        recipeRepository.save(newRecipe);
    }

    @Override
    public void deleteRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        recipeRepository.delete(recipe);
    }
}

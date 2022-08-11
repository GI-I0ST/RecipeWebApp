package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> addNewRecipe(@RequestBody @Valid Recipe newRecipe) {
        Long id = recipeService.addNewRecipe(newRecipe);
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editRecipe(@PathVariable Long id, @RequestBody @Valid Recipe newRecipe) {
        newRecipe.setId(id);
        recipeService.editRecipe(newRecipe);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipeById(@PathVariable Long id) {
        recipeService.deleteRecipeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

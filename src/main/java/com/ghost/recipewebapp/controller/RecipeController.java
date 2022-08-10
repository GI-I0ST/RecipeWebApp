package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.entity.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> addNewRecipe(@RequestBody @Valid Recipe newRecipe) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editRecipe(@PathVariable Long id, @RequestBody @Valid Recipe newRecipe) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipeById(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping("")
    public String getAllRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "recipes/index";
    }

    @GetMapping("/{id}")
    public String getRecipeById(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);
        return "recipes/recipePage";
    }

    @GetMapping("/new")
    public String getRecipeCreationForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "recipes/recipeForm";
    }

    @PostMapping("/new")
    public String addNewRecipe(@ModelAttribute("recipe") Recipe newRecipe) {
        Long id = recipeService.addNewRecipe(newRecipe);
        return "redirect:/recipe/" + id;
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

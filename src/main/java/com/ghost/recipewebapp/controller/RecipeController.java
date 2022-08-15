package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipes")
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
        return "redirect:/recipes/" + id;
    }

    @GetMapping("/{id}/edit")
    public String getRecipeEditForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);

        return "recipes/recipeForm";
    }

    @PutMapping("/{id}")
    public String editRecipe(@PathVariable Long id, @ModelAttribute("recipe") Recipe newRecipe) {
        newRecipe.setId(id);
        recipeService.editRecipe(newRecipe);
        return "redirect:/recipes/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteRecipeById(@PathVariable Long id) {
        recipeService.deleteRecipeById(id);
        return "redirect:/recipes";
    }
}

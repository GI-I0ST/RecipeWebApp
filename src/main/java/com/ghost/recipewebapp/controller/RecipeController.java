package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("")
    public String getAllRecipes(Model model,
                                @RequestParam(value = "page", required = false, defaultValue = "1")
                                        int currentPage,
                                @RequestParam(value = "page_size", required = false, defaultValue = "10")
                                        int pageSize) {
        if (currentPage < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter page less than 1");
        }
        if (pageSize < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter page_size less than 1");
        }

        Page<Recipe> page = recipeService.getRecipesPage(currentPage, pageSize);
        int totalPages = page.getTotalPages();

        if (currentPage != 1 && currentPage > totalPages) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter page greater than available");
        }

        model.addAttribute("current_page", currentPage);
        model.addAttribute("page_size", pageSize);
        model.addAttribute("total_pages", totalPages);
        model.addAttribute("total_elements", page.getTotalElements());
        model.addAttribute("recipes", page.getContent());
        model.addAttribute("last_page", page.isLast());
        model.addAttribute("first_page", page.isFirst());
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

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addNewRecipe(@Valid @ModelAttribute("recipe") Recipe newRecipe,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorForm", true);
            return "recipes/recipeForm";
        }

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
    public String editRecipe(@PathVariable Long id, @Valid @ModelAttribute("recipe") Recipe newRecipe, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorForm", true);
            return "recipes/recipeForm";
        }

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

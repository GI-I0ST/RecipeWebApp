package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.dto.RecipeDto;
import com.ghost.recipewebapp.dto.RecipeSearch;
import com.ghost.recipewebapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final String uploadedImagesDir;

    @Autowired
    public RecipeController(RecipeService recipeService,
                            @Value("${uploads.image-dir-mame}") String uploadedImagesDir) {
        this.recipeService = recipeService;
        this.uploadedImagesDir = uploadedImagesDir;
    }

    private List<Integer> calculatePageNumbers(Page<?> page, int currentPage) {
        int totalPages = page.getTotalPages();

        if (totalPages < 5) {
            return IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        } else {
            if (currentPage <= 2) {
                return IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());
            } else if (currentPage >= totalPages - 1) {
                return IntStream.rangeClosed(totalPages - 4, totalPages).boxed().collect(Collectors.toList());
            }
            return IntStream.rangeClosed(currentPage - 2, currentPage + 2).boxed().collect(Collectors.toList());

        }
    }

    @ModelAttribute
    private void addAttributes(Model model) {
        model.addAttribute("uploadedImagesDir", uploadedImagesDir);
    }

    @GetMapping("")
    public String getRecipesPage(Model model, @Valid @ModelAttribute("recipeSearch") RecipeSearch recipeSearch) {

        int currentPage = recipeSearch.getCurrentPageOrDefault();
        int pageSize = recipeSearch.getPageSizeOrDefault();

        Page<RecipeDto> recipePage = recipeService.getRecipesPage(recipeSearch);
        int totalPages = recipePage.getTotalPages();

        if (currentPage > totalPages) {
            currentPage = 1;
        }

        // content data
        model.addAttribute("recipes", recipePage.getContent());

        // pagination data
        model.addAttribute("page_numbers", calculatePageNumbers(recipePage, currentPage));
        model.addAttribute("current_page", currentPage);
        model.addAttribute("page_size", pageSize);
        model.addAttribute("last_page", recipePage.isLast());
        model.addAttribute("first_page", recipePage.isFirst());

        //link base
        model.addAttribute("filters_url_params", recipeSearch.getFilterParams());
        model.addAttribute("page_size_url_param", recipeSearch.getPageSizeParam());
        model.addAttribute("current_page_url_param", recipeSearch.getCurrentPageParam());

        return "recipes/index";
    }

    @GetMapping("/{id}")
    public String getRecipeById(@PathVariable Long id, Model model) {
        RecipeDto recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeSearch", new RecipeSearch());
        return "recipes/recipePage";
    }

    @GetMapping("/new")
    public String getRecipeCreationForm(Model model) {
        model.addAttribute("recipe", new RecipeDto());
        model.addAttribute("recipeSearch", new RecipeSearch());
        return "recipes/recipeForm";
    }

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addNewRecipe(@Valid @ModelAttribute("recipe") RecipeDto newRecipe, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorForm", true);
            model.addAttribute("recipeSearch", new RecipeSearch());
            return "recipes/recipeForm";
        }

        Long id = recipeService.addNewRecipe(newRecipe);
        return "redirect:/recipes/" + id;
    }

    @GetMapping("/{id}/edit")
    public String getRecipeEditForm(@PathVariable Long id, Model model) {
        RecipeDto recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeSearch", new RecipeSearch());

        return "recipes/recipeForm";
    }

    @PutMapping("/{id}")
    public String editRecipe(@PathVariable Long id, @Valid @ModelAttribute("recipe") RecipeDto newRecipe, BindingResult bindingResult, Model model) {
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

    @PostMapping("/{id}/favourites")
    @ResponseBody
    public HttpStatus addRecipeToFavourites(@PathVariable Long id) {
        recipeService.addToFavourites(id);

        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}/favourites")
    @ResponseBody
    public HttpStatus deleteRecipeFromFavourites(@PathVariable Long id) {
        recipeService.removeFromFavourites(id);

        return HttpStatus.OK;
    }
}

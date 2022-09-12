package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.service.AutocompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@Validated
@ComponentScan("com.ghost.service")
public class AutocompleteController {
    @Autowired
    private AutocompleteService autocompleteService;

    // returns "label" for Ingredient.product
    @GetMapping("/ingredients")
    public ResponseEntity<List<String>> searchProducts(@RequestParam("product")
                                                       @NotBlank(message = "product param is required")
                                                               String input) {
        return new ResponseEntity<>(autocompleteService.getProductsContainsStr(input), HttpStatus.OK);
    }

    // returns "value" and "label" for Recipe.title
    @GetMapping("/titles")
    public ResponseEntity<List<Map<String, String>>> searchTitlesAndId(@RequestParam("title")
                                                                       @NotBlank(message = "title param is required")
                                                                               String input) {
        return new ResponseEntity<>(autocompleteService.getTitlesContainsStr(input), HttpStatus.OK);
    }
}

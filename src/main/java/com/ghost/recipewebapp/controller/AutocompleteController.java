package com.ghost.recipewebapp.controller;

import com.ghost.recipewebapp.service.impl.AutocompleteService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AutocompleteController {
    @Autowired
    private AutocompleteService autocompleteService;

    // returns "label" for Ingredient.product
    @GetMapping("/ingredients")
    public ResponseEntity<List<String>> searchProducts(@RequestParam("product") @NotBlank String input) {
        return new ResponseEntity<>(autocompleteService.getProductsContainsStr(input), HttpStatus.OK);
    }

    // returns "value" and "label" for Recipe.title
    @GetMapping("/titles")
    public ResponseEntity<List<Map<String, String>>> searchTitlesAndId(@RequestParam("title") @NotBlank String input) {
        return new ResponseEntity<>(autocompleteService.getTitlesContainsStr(input), HttpStatus.OK);
    }
}

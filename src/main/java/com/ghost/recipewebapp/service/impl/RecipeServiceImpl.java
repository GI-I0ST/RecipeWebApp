package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.dto.RecipeDto;
import com.ghost.recipewebapp.entity.MultipartImageEntity;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.repository.RecipeRepository;
import com.ghost.recipewebapp.dto.RecipeSearch;
import com.ghost.recipewebapp.repository.specification.RecipeSpecifications;
import com.ghost.recipewebapp.service.RecipeService;
import com.ghost.recipewebapp.util.FileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@ComponentScan("com.ghost.util")
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final FileLoader fileLoader;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, FileLoader fileLoader) {
        this.recipeRepository = recipeRepository;
        this.fileLoader = fileLoader;
    }

    private RecipeDto recipeToRecipeDto (Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setImage(recipe.getImage());
        recipeDto.setHours(recipe.getTime() / 60);
        recipeDto.setMinutes(recipe.getTime() % 60);
        recipeDto.setCalories(recipe.getCalories());

        recipeDto.getIngredientsList().addAll(recipe.getIngredientsList());
        recipeDto.getStepsList().addAll(recipe.getStepsList());

        return recipeDto;
    }

    private void uploadImage(MultipartImageEntity imageEntity) {
        MultipartFile imageMultipart = imageEntity.getImageMultipart();

        if (Objects.nonNull(imageMultipart) && !imageMultipart.isEmpty()) {
            String imgName = fileLoader.uploadFile(imageMultipart);
            imageEntity.setImage(imgName);
        }
    }

    private void deleteImage(MultipartImageEntity imageEntity) {
        String imgName = imageEntity.getImage();

        if (Objects.nonNull(imgName) && !imgName.isBlank()) {
            fileLoader.deleteFile(imgName);
            imageEntity.setImage(null);
        }
    }

    private void updateImage(MultipartImageEntity imageEntity) {
        MultipartFile imageMultipart = imageEntity.getImageMultipart();

        if (Objects.nonNull(imageMultipart) && !imageMultipart.isEmpty()) {
            deleteImage(imageEntity);
            uploadImage(imageEntity);
        }
    }

    @Override
    public Long addNewRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();

        // upload recipe image
        uploadImage(recipeDto);
        recipe.setImage(recipeDto.getImage());
        recipe.setTitle(recipeDto.getTitle());
        // convert to minutes
        recipe.setTime(recipeDto.getHours() * 60 + recipeDto.getMinutes());
        recipe.setCalories(recipeDto.getCalories());
        recipe.getIngredientsList().addAll(recipeDto.getIngredientsList());
        recipe.getStepsList().addAll(recipeDto.getStepsList());
        // upload step image
        recipeDto.getStepsList().forEach(this::uploadImage);
        recipeRepository.saveAndFlush(recipe);

        return recipe.getId();
    }

    @Override
    public Page<RecipeDto> getRecipesPage(RecipeSearch recipeSearch) {
        Pageable pageable = PageRequest.of(recipeSearch.getCurrentPageOrDefault() - 1,
                recipeSearch.getPageSizeOrDefault());

        Specification<Recipe> spec = Specification.where(RecipeSpecifications.likeTitle(recipeSearch.getTitle()))
                .and(RecipeSpecifications.withImage(recipeSearch.getImage()))
                .and(RecipeSpecifications.timeLessOrEqual(recipeSearch.getTime()))
                .and(RecipeSpecifications.caloriesLessOrEqual(recipeSearch.getCalories()))
                .and(RecipeSpecifications.containsProducts(recipeSearch.getProducts()));

        Page<Recipe> recipePage = recipeRepository.findAll(spec, pageable);

        return recipePage.map(this::recipeToRecipeDto);
    }

    @Override
    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Recipe with id " + id + " not found"));

        return recipeToRecipeDto(recipe);
    }

    @Override
    public void editRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(recipeDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Recipe with id " + recipeDto.getId() + " not found"));

        recipe.setTitle(recipeDto.getTitle());

        // edit image
        recipeDto.setImage(recipe.getImage());
        updateImage(recipeDto);
        recipe.setImage(recipeDto.getImage());

        recipeDto.getStepsList().forEach(step -> {
            // remove already uploaded image from deletion list
            if (Objects.nonNull(step.getImage()) && !step.getImage().isBlank() && step.getImageMultipart().isEmpty()) {
                recipe.getStepsList().removeIf(oldStep -> step.getImage().equals(oldStep.getImage()));
            }

            // upload new step image
            uploadImage(step);
        });
        // delete unused uploaded step images
        recipe.getStepsList().forEach(this::deleteImage);
        // set new steps list
        recipe.getStepsList().clear();
        recipe.getStepsList().addAll(recipeDto.getStepsList());

        recipe.setTime(recipeDto.getHours() * 60 + recipeDto.getMinutes());
        recipe.setCalories(recipeDto.getCalories());
        recipe.getIngredientsList().clear();
        recipe.getIngredientsList().addAll(recipeDto.getIngredientsList());

        recipeRepository.save(recipe);
    }

    @Override
    public void deleteRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        deleteImage(recipeToRecipeDto(recipe));
        recipe.getStepsList().forEach(this::deleteImage);

        recipeRepository.delete(recipe);
    }
}

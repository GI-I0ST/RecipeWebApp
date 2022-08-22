package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.AbstractMultipartImageEntity;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.repository.RecipeRepository;
import com.ghost.recipewebapp.service.RecipeService;
import com.ghost.recipewebapp.util.FileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    private void uploadImage(AbstractMultipartImageEntity imageEntity) {
        MultipartFile imageMultipart = imageEntity.getImageMultipart();

        if (Objects.nonNull(imageMultipart) && !imageMultipart.isEmpty()) {
            String imgName = FileLoader.uploadFile(imageMultipart);
            imageEntity.setImage(imgName);
        }
    }

    private void deleteImage(AbstractMultipartImageEntity imageEntity) {
        String imgName = imageEntity.getImage();

        if (Objects.nonNull(imgName) && !imgName.isBlank()) {
            FileLoader.deleteFile(imgName);
            imageEntity.setImage(null);
        }
    }

    private void updateImage(AbstractMultipartImageEntity imageEntity) {
        MultipartFile imageMultipart = imageEntity.getImageMultipart();

        if (Objects.nonNull(imageMultipart) && !imageMultipart.isEmpty()) {
            deleteImage(imageEntity);
            uploadImage(imageEntity);
        }
    }

    @Override
    public Long addNewRecipe(Recipe newRecipe) {
        // add reference to steps
        newRecipe.getStepsList().forEach(step -> {
            step.setRecipe(newRecipe);
        });

        //upload images
        uploadImage(newRecipe);
        newRecipe.getStepsList().forEach(this::uploadImage);

        recipeRepository.save(newRecipe);

        return newRecipe.getId();
    }

    @Override
    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void editRecipe(Recipe newRecipe) {
        Recipe oldRecipe = recipeRepository.findById(newRecipe.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        newRecipe.setCommentsList(oldRecipe.getCommentsList());
        newRecipe.getStepsList().forEach(step -> step.setRecipe(newRecipe));

        updateImage(newRecipe);
        newRecipe.getStepsList().forEach(this::updateImage);

        recipeRepository.save(newRecipe);
    }

    @Override
    public void deleteRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        deleteImage(recipe);
        recipe.getStepsList().forEach(this::deleteImage);

        recipeRepository.delete(recipe);
    }
}

package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.AbstractMultipartImageEntity;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.repository.RecipeRepository;
import com.ghost.recipewebapp.service.RecipeService;
import com.ghost.recipewebapp.util.FileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    private void uploadImage(AbstractMultipartImageEntity imageEntity) {
        MultipartFile imageMultipart = imageEntity.getImageMultipart();

        if (Objects.nonNull(imageMultipart) && !imageMultipart.isEmpty()) {
            String imgName = fileLoader.uploadFile(imageMultipart);
            imageEntity.setImage(imgName);
        }
    }

    private void deleteImage(AbstractMultipartImageEntity imageEntity) {
        String imgName = imageEntity.getImage();

        if (Objects.nonNull(imgName) && !imgName.isBlank()) {
            fileLoader.deleteFile(imgName);
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
        // for steps
        newRecipe.getStepsList().forEach(step -> {
            // add reference to parent
            step.setRecipe(newRecipe);
            // upload image
            this.uploadImage(step);
        });

        //upload images
        uploadImage(newRecipe);

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

        // add comments
        newRecipe.setCommentsList(oldRecipe.getCommentsList());

        newRecipe.getStepsList().forEach(step -> {
            // add reference to parent
            step.setRecipe(newRecipe);

            // remove old uploaded image from deletion list
            if (!step.getImage().isBlank() && step.getImageMultipart().isEmpty()) {
                oldRecipe.getStepsList().removeIf(oldStep -> oldStep.getImage().equals(step.getImage()));
            }

            // upload image
            this.uploadImage(step);
        });

        // delete old uploaded images from steps
        oldRecipe.getStepsList().forEach(this::deleteImage);

        // edit image
        newRecipe.setImage(oldRecipe.getImage());
        updateImage(newRecipe);

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

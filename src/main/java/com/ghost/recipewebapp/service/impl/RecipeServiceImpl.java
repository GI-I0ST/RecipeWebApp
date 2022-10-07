package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.Step;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.UserDetailsImpl;
import com.ghost.recipewebapp.exception.RecipeNotFoundException;
import com.ghost.recipewebapp.repository.RecipeRepository;
import com.ghost.recipewebapp.dto.RecipeSearch;
import com.ghost.recipewebapp.repository.UserRepository;
import com.ghost.recipewebapp.repository.specification.RecipeSpecifications;
import com.ghost.recipewebapp.service.RecipeService;
import com.ghost.recipewebapp.util.FileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final FileLoader fileLoader;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             UserRepository userRepository,
                             @Value("${uploads.image-dir-mame}") String uploadsImageDir,
                             @Value("${uploads.static-dir}") String uploadsDir) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.fileLoader = new FileLoader(Path.of(uploadsDir, uploadsImageDir));
    }

    private Optional<String> uploadImage(MultipartFile imageMultipart) {
        if (Objects.nonNull(imageMultipart) && !imageMultipart.isEmpty()) {
            String imgName = fileLoader.uploadFile(imageMultipart);
            return Optional.of(imgName);
        }

        return Optional.empty();
    }

    private void deleteImage(String imgName) {
        if (Objects.nonNull(imgName) && !imgName.isBlank()) {
            fileLoader.deleteFile(imgName);
        }
    }

    @Override
    public Long addNewRecipe(Recipe newRecipe) {
        // upload recipe image
        uploadImage(newRecipe.getImageMultipart()).
                ifPresent(newRecipe::setImage);
        // upload step image
        newRecipe.getStepsList().forEach(
                step -> uploadImage(step.getImageMultipart())
                        .ifPresent(step::setImage)
        );

        recipeRepository.saveAndFlush(newRecipe);

        return newRecipe.getId();
    }

    @Override
    public Page<Recipe> getRecipesPage(RecipeSearch recipeSearch) {
        Pageable pageable = PageRequest.of(recipeSearch.getCurrentPageOrDefault() - 1,
                recipeSearch.getPageSizeOrDefault());

        Specification<Recipe> spec = Specification.where(RecipeSpecifications.likeTitle(recipeSearch.getTitle()))
                .and(RecipeSpecifications.withImage(recipeSearch.getImage()))
                .and(RecipeSpecifications.timeLessOrEqual(recipeSearch.getTime()))
                .and(RecipeSpecifications.caloriesLessOrEqual(recipeSearch.getCalories()))
                .and(RecipeSpecifications.containsProducts(recipeSearch.getProducts()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            spec = spec.and(RecipeSpecifications.inFavourites(recipeSearch.getInFavourites(), authentication.getName()));
        }

        return recipeRepository.findAll(spec, pageable);
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with id " + id + " not found"));
    }

    @Override
    public void editRecipe(Recipe newRecipe) {
        Recipe foundRecipe = this.getRecipeById(newRecipe.getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        // if not owner
        if (!foundRecipe.getAuthor().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only owner can edit recipe");
        }

        // edit image
        uploadImage(newRecipe.getImageMultipart())
                .ifPresentOrElse((imgName) -> {
                            deleteImage(foundRecipe.getImage());
                            newRecipe.setImage(imgName);
                        },
                        () -> newRecipe.setImage(foundRecipe.getImage()));

        for (Step step : newRecipe.getStepsList()) {
            // remove already uploaded step image from deletion list
            if (Objects.nonNull(step.getImage()) && !step.getImage().isBlank() && step.getImageMultipart().isEmpty()) {
                foundRecipe.getStepsList().removeIf(oldStep -> step.getImage().equals(oldStep.getImage()));
                continue;
            }

            // upload new step image
            uploadImage(step.getImageMultipart())
                    .ifPresent(step::setImage);
        }
        // delete unused uploaded step images
        foundRecipe.getStepsList().forEach(oldStep -> deleteImage(oldStep.getImage()));

        // copy external fields
        newRecipe.setAuthor(foundRecipe.getAuthor());
        newRecipe.setLikedUsers(foundRecipe.getLikedUsers());

        recipeRepository.save(newRecipe);
    }

    @Override
    public void deleteRecipeById(Long id) {
        Recipe recipe = this.getRecipeById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        // if not owner or not admin
        if (!recipe.getAuthor().equals(currentUser)
                && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only owner or admin can delete recipe");
        }

        deleteImage(recipe.getImage());
        recipe.getStepsList().forEach(step -> deleteImage(step.getImage()));

        recipeRepository.delete(recipe);
    }

    @Override
    public void addToFavourites(Long recipeId) {
        Recipe recipe = this.getRecipeById(recipeId);

        //get authenticated email from SecurityContextHolder
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email" + email + "not found"));
        recipe.addToLikedUsers(user);

        recipeRepository.save(recipe);
    }

    @Override
    public void removeFromFavourites(Long recipeId) {
        Recipe recipe = this.getRecipeById(recipeId);

        //get authenticated email from SecurityContextHolder
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email" + email + "not found"));

        recipe.removeFromLikedUsers(user);

        recipeRepository.save(recipe);
    }
}

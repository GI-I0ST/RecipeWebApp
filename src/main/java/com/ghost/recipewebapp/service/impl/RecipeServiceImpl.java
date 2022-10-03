package com.ghost.recipewebapp.service.impl;

import com.ghost.recipewebapp.dto.RecipeDto;
import com.ghost.recipewebapp.dto.RecipeFullDto;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.Step;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.modelMapper.RecipeMapper;
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
    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             UserRepository userRepository,
                             @Value("${uploads.image-dir-mame}") String uploadsImageDir,
                             @Value("${uploads.static-dir}") String uploadsDir,
                             RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.fileLoader = new FileLoader(Path.of(uploadsDir, uploadsImageDir));
        this.recipeMapper = recipeMapper;
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
    public Long addNewRecipe(RecipeFullDto recipeDto) {
        // upload recipe image
        uploadImage(recipeDto.getImageMultipart()).
                ifPresent(recipeDto::setImage);
        // upload step image
        recipeDto.getStepsList().forEach(
                step -> uploadImage(step.getImageMultipart())
                        .ifPresent(step::setImage)
        );

        Recipe recipe = recipeMapper.toEntity(recipeDto);
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            spec = spec.and(RecipeSpecifications.inFavourites(recipeSearch.getInFavourites(), authentication.getName()));
        }

        Page<Recipe> recipePage = recipeRepository.findAll(spec, pageable);

        return recipePage.map(recipeMapper::toShortDto);
    }

    @Override
    public RecipeFullDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Recipe with id " + id + " not found"));

        return recipeMapper.toFullDto(recipe);
    }

    @Override
    public void editRecipe(RecipeFullDto recipeDto) {
        Recipe foundRecipe = recipeRepository.findById(recipeDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Recipe with id " + recipeDto.getId() + " not found"));

        // edit image
        recipeDto.setImage(foundRecipe.getImage());
        uploadImage(recipeDto.getImageMultipart())
                .ifPresent((imgName) -> {
                    deleteImage(recipeDto.getImage());
                    recipeDto.setImage(imgName);
                });

        for (Step step : recipeDto.getStepsList()) {
            // remove already uploaded image from deletion list
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
        Recipe newRecipe = recipeMapper.toEntity(recipeDto);
        newRecipe.setAuthor(foundRecipe.getAuthor());
        newRecipe.setLikedUsers(foundRecipe.getLikedUsers());

        recipeRepository.save(newRecipe);
    }

    @Override
    public void deleteRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        deleteImage(recipe.getImage());
        recipe.getStepsList().forEach(step -> deleteImage(step.getImage()));

        recipeRepository.delete(recipe);
    }

    @Override
    public void addToFavourites(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + recipeId + "not found"));

        //get authenticated email from SecurityContextHolder
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email" + email + "not found"));
        recipe.addToLikedUsers(user);

        recipeRepository.save(recipe);
    }

    @Override
    public void removeFromFavourites(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + recipeId + "not found"));

        //get authenticated email from SecurityContextHolder
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email" + email + "not found"));

        recipe.addToLikedUsers(user);

        recipeRepository.save(recipe);
    }
}

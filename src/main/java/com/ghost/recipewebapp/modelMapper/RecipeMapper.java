package com.ghost.recipewebapp.modelMapper;

import com.ghost.recipewebapp.dto.RecipeDto;
import com.ghost.recipewebapp.dto.RecipeFullDto;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Component
public class RecipeMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public RecipeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        TypeMap<Recipe, RecipeDto> baseTypeMap = modelMapper.createTypeMap(Recipe.class, RecipeDto.class)
                .addMappings(mapper -> mapper
                        .using(ctx -> ((int) ctx.getSource()) / 60)
                        .map(Recipe::getTime, RecipeDto::setHours))
                .addMappings(mapper -> mapper
                        .using(ctx -> ((int) ctx.getSource()) % 60)
                        .map(Recipe::getTime, RecipeDto::setMinutes))
                .addMapping(recipe -> recipe.getAuthor().getName(), RecipeDto::setAuthor)
                .addMappings(mapper -> mapper
                        .using(ctx -> {
                            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                            if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                                User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
                                return ((Collection<User>) ctx.getSource()).contains(user);
                            }
                            return false;
                        })
                        .map(Recipe::getLikedUsers, RecipeDto::setFavourite));

        baseTypeMap.include(Recipe.class, RecipeFullDto.class);
        modelMapper.typeMap(Recipe.class, RecipeFullDto.class)
                .addMapping(Recipe::getStepsList, RecipeFullDto::setStepsList)
                .addMapping(Recipe::getIngredientsList, RecipeFullDto::setIngredientsList);

        modelMapper.createTypeMap(RecipeFullDto.class, Recipe.class)
                .addMappings(mapper -> mapper.skip(Recipe::setAuthor))
                .addMappings(mapper -> mapper.skip(Recipe::setLikedUsers))
                .addMappings(
                        new PropertyMap<RecipeFullDto, Recipe>() {
                            @Override
                            protected void configure() {
                                using(ctx ->
                                        ((RecipeFullDto) ctx.getSource()).getHours() * 60 + ((RecipeFullDto) ctx.getSource()).getMinutes()
                                ).map(source, destination.getTime());
                            }
                        });
    }

    public Recipe toEntity(@NotNull RecipeFullDto recipeDto) {
        return modelMapper.map(recipeDto, Recipe.class);
    }

    public RecipeFullDto toFullDto(@NotNull Recipe recipe) {
        return modelMapper.map(recipe, RecipeFullDto.class);
    }

    public RecipeDto toShortDto(@NotNull Recipe recipe) {
        return modelMapper.map(recipe, RecipeDto.class);
    }
}

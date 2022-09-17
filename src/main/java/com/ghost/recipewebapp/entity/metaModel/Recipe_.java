package com.ghost.recipewebapp.entity.metaModel;

import com.ghost.recipewebapp.entity.Ingredient;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.Step;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Recipe.class)
public abstract class Recipe_ {
    public static volatile SingularAttribute<Recipe, Long> id;
    public static volatile SingularAttribute<Recipe, String> title;
    public static volatile SingularAttribute<Recipe, String> image;
    public static volatile SingularAttribute<Recipe, Integer> time;
    public static volatile SingularAttribute<Recipe, Integer> calories;
    public static volatile ListAttribute<Recipe, Ingredient> ingredientsList;
    public static volatile ListAttribute<Recipe, Step> stepsList;

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String TIME = "time";
    public static final String CALORIES = "calories";
    public static final String INGREDIENTS_LIST = "ingredientsList";
    public static final String STEPS_LIST = "stepsList";
}

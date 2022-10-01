package com.ghost.recipewebapp.entity.metaModel;

import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.User;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

public class User_ {
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, String> authorities;
    public static volatile SetAttribute<User, Recipe> favouriteRecipes;

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String AUTHORITIES = "authorities";
    public static final String FAVOURITE_RECIPES = "favouriteRecipes";
}

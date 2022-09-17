package com.ghost.recipewebapp.entity.metaModel;

import com.ghost.recipewebapp.entity.Ingredient;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Ingredient.class)
public class Ingredient_ {
    public static volatile SingularAttribute<Ingredient, Long> id;
    public static volatile SingularAttribute<Ingredient, String> product;
    public static volatile SingularAttribute<Ingredient, String> measure;

    public static final String ID = "id";
    public static final String PRODUCT = "product";
    public static final String MEASURE = "measure";
}

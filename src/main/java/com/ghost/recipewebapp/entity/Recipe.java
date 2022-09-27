package com.ghost.recipewebapp.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    // image name
    @Column(name = "image")
    private String image;

    @Column(name = "title")
    private String title;

    // time in minutes
    @Column(name = "time")
    private int time;

    // calories in kcal
    @Column(name = "calories")
    private int calories;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredientsList = new ArrayList<>();

    //steps to cook
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> stepsList = new ArrayList<>();

    //comments to recipe
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentsList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Recipe recipe = (Recipe) o;
        return id != null && Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
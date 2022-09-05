package com.ghost.recipewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe extends AbstractMultipartImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Long id;

    // image name
    @Column(name = "image")
    private String image;

    @Column(name = "title")
    @Size(min = 5, message = "Title must contain at least 5 symbols")
    @NotBlank(message = "Title is required")
    private String title;

    // time in minutes
    @Column(name = "time")
    @Min(value = 1, message = "Time must be greater than 1 minute")
    @Max(value = 10_080, message = "Time must be less than 7 days")
    private int time;

    @Formula("time / 60")
    @Min(value = 0, message = "Hours must be positive")
    private int hours;

    public void setHours(int hours) {
        this.hours = hours;

        this.time = hours * 60 + this.minutes;
    }

    @Formula("time % 60")
    @Min(value = 0, message = "Minutes must be positive")
    @Max(value = 59, message = "Minutes must be less than 60")
    private int minutes;

    public void setMinutes(int minutes) {
        this.minutes = minutes;

        this.time = this.hours * 60 + minutes;
    }

    // calories in kcal
    @Column(name = "calories")
    @Min(value = 0, message = "Calories must be positive")
    private int calories;

    @Column(name = "ingredients")
    @NotBlank(message = "Ingredients required")
    private String ingredients;

    //steps to cook
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 1, message = "Recipe must contain at least 1 step")
    @Valid
    private List<Step> stepsList = new ArrayList<>();

    //comments to recipe
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
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
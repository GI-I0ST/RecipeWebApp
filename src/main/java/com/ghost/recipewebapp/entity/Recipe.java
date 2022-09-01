package com.ghost.recipewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

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
    @NotBlank
    private String title;

    // time in minutes
    @Column(name = "time")
    @Min(1)
    @Max(144_000)
    private int time;

    // calories in kcal
    @Column(name = "calories")
    private int calories;

    @Column(name = "ingredients")
    @NotBlank
    private String ingredients;

    //delete cascade in deploy
    //!!! only for testing
    /*
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    */

    //steps to cook
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 1)
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
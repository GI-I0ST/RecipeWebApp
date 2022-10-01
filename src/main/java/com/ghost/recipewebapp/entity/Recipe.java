package com.ghost.recipewebapp.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

@EntityListeners(AuditingEntityListener.class)
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

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "favourites_users",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private Set<User> likedUsers = new HashSet<>();

    public void addToLikedUsers(User user) {
        this.likedUsers.add(user);
        user.getFavouriteRecipes().add(this);
    }

    public void removeFromLikedUsers(User user) {
        this.likedUsers.remove(user);
        user.getFavouriteRecipes().remove(this);
    }

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
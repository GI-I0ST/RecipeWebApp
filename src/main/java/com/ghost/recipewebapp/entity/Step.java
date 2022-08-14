package com.ghost.recipewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "steps")
public class Step {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @ToString.Exclude
    private Long id;

    @Column(name = "text")
    @NotBlank
    private String text;

    //images for step
    @ElementCollection
    @Column(name = "image")
    @CollectionTable(name = "step_images", joinColumns = @JoinColumn(name = "step_id"))
    private List<String> imageList = new ArrayList<>();

    //parent
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    @ToString.Exclude
    private Recipe recipe;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Step step = (Step) o;
        return id != null && Objects.equals(id, step.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

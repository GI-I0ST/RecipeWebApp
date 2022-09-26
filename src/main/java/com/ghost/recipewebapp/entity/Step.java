package com.ghost.recipewebapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "steps")
public class Step extends AbstractMultipartImageEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    @NotBlank(message = "Step's text is required")
    private String text;

    //images for step
    @Column(name = "image")
    private String image;

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

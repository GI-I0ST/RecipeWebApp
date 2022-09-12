package com.ghost.recipewebapp.repository.impl;

import com.ghost.recipewebapp.entity.Ingredient;
import com.ghost.recipewebapp.repository.AutocompleteIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Repository
public class AutocompleteIngredientRepositoryImpl implements AutocompleteIngredientRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    @Autowired
    public AutocompleteIngredientRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public List<String> findProductsContainsStr(String input) {
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);

        Root<Ingredient> ingredient = criteriaQuery.from(Ingredient.class);

        Predicate like = criteriaBuilder.like(criteriaBuilder.lower(ingredient.get("product")),
                "%" + input.toLowerCase() + "%");
        criteriaQuery.select(ingredient.get("product"))
                .where(like)
                .groupBy(ingredient.get("product"))
                .orderBy(criteriaBuilder.asc(ingredient.get("product")));

        return entityManager.createQuery(criteriaQuery).setMaxResults(10).getResultList();
    }
}

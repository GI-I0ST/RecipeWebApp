package com.ghost.recipewebapp.repository.specification;

import com.ghost.recipewebapp.entity.Ingredient;
import com.ghost.recipewebapp.entity.Recipe;
import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.metaModel.Ingredient_;
import com.ghost.recipewebapp.entity.metaModel.Recipe_;
import com.ghost.recipewebapp.entity.metaModel.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeSpecifications {
    public static Specification<Recipe> likeTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(title) || title.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get(Recipe_.TITLE), "%" + title + "%");
        };
    }

    public static Specification<Recipe> withImage(Boolean image) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(image) || !image) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.isNotNull(root.get(Recipe_.IMAGE));
        };
    }

    public static Specification<Recipe> timeLessOrEqual(Integer time) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(time)) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Recipe_.TIME), time);
        };
    }

    public static Specification<Recipe> caloriesLessOrEqual(Integer calories) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(calories)) {
                return criteriaBuilder.conjunction();
            }

            Predicate notNullPredicate = criteriaBuilder.isNotNull(root.get(Recipe_.CALORIES));
            Predicate equalsOrLessPredicate = criteriaBuilder.lessThanOrEqualTo(root.get(Recipe_.CALORIES), calories);

            return criteriaBuilder.and(notNullPredicate, equalsOrLessPredicate);
        };
    }

    public static Specification<Recipe> containsProducts(List<String> products) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(products) || products.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            for (String product : products) {
                if (product.isBlank()) {
                    continue;
                }
                Join<Recipe, Ingredient> ingredientRecipeJoin = root.join(Recipe_.INGREDIENTS_LIST, JoinType.INNER);
                predicates.add(criteriaBuilder.equal(ingredientRecipeJoin.get(Ingredient_.PRODUCT), product));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Recipe> inFavourites(Boolean inFavourites, String email) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(inFavourites) || !inFavourites) {
                return criteriaBuilder.conjunction();
            }

            Join<Recipe, User> userRecipeJoin = root.join(Recipe_.LIKED_USERS, JoinType.INNER);
            return criteriaBuilder.equal(userRecipeJoin.get(User_.EMAIL), email);
        };
    }
}

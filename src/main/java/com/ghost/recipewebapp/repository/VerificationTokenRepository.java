package com.ghost.recipewebapp.repository;

import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}

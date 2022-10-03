package com.ghost.recipewebapp.config;

import com.ghost.recipewebapp.entity.User;
import com.ghost.recipewebapp.entity.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

class SpringSecurityAuditorAware implements AuditorAware<User> {
    // get authenticated User from SecurityContextHolder
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Optional.of(userDetails.getUser());
    }
}
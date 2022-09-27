package com.ghost.recipewebapp.config;

import com.ghost.recipewebapp.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Define Jpa Auditor
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditInitializer {
    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}


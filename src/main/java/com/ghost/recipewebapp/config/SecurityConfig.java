package com.ghost.recipewebapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                    .mvcMatchers("/recipes/new").authenticated()
                    .mvcMatchers(HttpMethod.GET, "/recipes", "/recipes/*").permitAll()
                    .mvcMatchers(HttpMethod.GET, "/search/*").permitAll()
                    .mvcMatchers("/register").permitAll()
                    .mvcMatchers("/registrationConfirm").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/recipes")
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/recipes")
                    .deleteCookies("JSESSIONID")
                    .permitAll();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login");

        return http.build();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // ignore resources
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(@Value("${uploads.image-dir-mame}") String uploadsImageDir) {
        return (web) -> web
                .ignoring()
                .mvcMatchers("/assets/**", "/" + uploadsImageDir + "/**");
    }
}

package com.ghost.recipewebapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

// Config for server uploads
@Validated
@Configuration
@ConfigurationProperties(prefix = "uploads")
@Getter
@Setter
public class UploadsConfig {
    @NotEmpty
    private String staticDir;
    @NotEmpty
    private String tmpDir;
    @NotEmpty
    private String imageDirMame;

}
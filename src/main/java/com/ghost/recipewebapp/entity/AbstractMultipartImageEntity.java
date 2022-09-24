package com.ghost.recipewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

// Abstract class for entities with field uses MultipartFile for image
@NoArgsConstructor
@Setter
@Getter
@MappedSuperclass
public abstract class AbstractMultipartImageEntity {
    @Transient
    @JsonIgnore
    private MultipartFile imageMultipart;

    public abstract String getImage();
    public abstract void setImage(String image);
}

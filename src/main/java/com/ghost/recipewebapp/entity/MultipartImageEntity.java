package com.ghost.recipewebapp.entity;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.MappedSuperclass;

// Interface for entities with field uses MultipartFile for image
@MappedSuperclass
public interface MultipartImageEntity {
    MultipartFile getImageMultipart();

    void setImageMultipart(MultipartFile imageMultipart);

    String getImage();

    void setImage(String image);
}

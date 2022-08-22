package com.ghost.recipewebapp.util;

import com.ghost.recipewebapp.exception.EnvVarNotFoundException;
import com.ghost.recipewebapp.exception.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileLoader {

    private final Environment env;
    public final String UPLOAD_DIRECTORY;

    @Autowired
    public FileLoader(Environment env, Environment env1) {
        this.env = env1;
        String path = env.getProperty("UPLOADS_RESOURCE_PATH");
        if (Objects.isNull(path)) {
            throw new EnvVarNotFoundException("Environment variable 'UPLOADS_RESOURCE_PATH' not found");
        }
        UPLOAD_DIRECTORY = Path.of(path, "uploadedImages").toString();
    }

    public String uploadFile(MultipartFile file) {
        //file name as UUID + extension
        String newName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        //move file from temp dir to UPLOAD_DIRECTORY
        try {
            File transferFile = new File(UPLOAD_DIRECTORY, newName);
            file.transferTo(transferFile);
        } catch (IOException e) {
            throw new FileUploadException("File " + file.getOriginalFilename() + " failed while uploading", e);
        }

        return newName;
    }

    public void deleteFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(UPLOAD_DIRECTORY, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

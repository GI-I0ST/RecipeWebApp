package com.ghost.recipewebapp.util;

import com.ghost.recipewebapp.exception.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.UUID;

public class FileLoader {

    public static String UPLOAD_DIRECTORY = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "uploads").toString();


    public static String uploadFile(MultipartFile file) {
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

    public static void deleteFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(UPLOAD_DIRECTORY, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

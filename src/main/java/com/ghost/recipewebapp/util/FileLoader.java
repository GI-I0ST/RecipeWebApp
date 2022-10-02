package com.ghost.recipewebapp.util;

import com.ghost.recipewebapp.exception.FileLoaderException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.UUID;

@Validated
public class FileLoader {

    public final String uploadDirectory;

    public FileLoader(@NotNull Path path) {
        Path absolutePath = path.normalize().toAbsolutePath();
        // if path exists and is directory
        if (!Files.isDirectory(absolutePath)) {
            throw new FileLoaderException("Directory " + absolutePath + " is not found");
        }

        uploadDirectory = absolutePath.toString();
    }

    public String uploadFile(MultipartFile file) {
        //file name as UUID + extension
        String newName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        //move file from temp dir to uploadDirectory
        try {
            File transferFile = new File(uploadDirectory, newName);
            file.transferTo(transferFile);
        } catch (IOException e) {
            throw new FileLoaderException("File " + file.getOriginalFilename() + " failed while uploading", e);
        }

        return newName;
    }

    public void deleteFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(uploadDirectory, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

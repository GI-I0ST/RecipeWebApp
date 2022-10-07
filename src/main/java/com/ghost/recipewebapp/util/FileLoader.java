package com.ghost.recipewebapp.util;

import com.ghost.recipewebapp.exception.FileLoaderException;
import com.ghost.recipewebapp.exception.FileLoaderIOException;
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

    /**
     * Path of directory for usage
     */
    public final String uploadDirectory;


    /**
     * @param path path of directory for usage
     * @throws FileLoaderException when path is not found or not a directory
     */
    public FileLoader(@NotNull Path path) {
        Path absolutePath = path.normalize().toAbsolutePath();
        // if path exists and is directory
        if (!Files.isDirectory(absolutePath)) {
            throw new FileLoaderException("Directory " + absolutePath + " is not found");
        }

        uploadDirectory = absolutePath.toString();
    }

    /**
     * Uploads multipart to specified directory {@link #uploadDirectory}
     *
     * @param file multipart that should be uploaded
     * @return new name of uploaded file
     * @throws FileLoaderIOException in case of reading or writing errors
     * @throws IllegalStateException see {@link MultipartFile#transferTo(File)}
     */
    public String uploadFile(@NotNull MultipartFile file) {
        //file name as UUID + extension
        String newName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        //move file from temp dir to uploadDirectory
        try {
            File transferFile = new File(uploadDirectory, newName);
            file.transferTo(transferFile);
        } catch (IOException e) {
            throw new FileLoaderIOException("File " + file.getOriginalFilename() + " failed while uploading", e);
        }

        return newName;
    }

    /**
     * Deletes file from specified directory {@link #uploadDirectory}
     *
     * @param fileName name of file with extension
     * @throws FileLoaderIOException in case of reading or writing errors
     * @throws SecurityException     see {@link Files#deleteIfExists(Path)}
     */
    public void deleteFile(@NotNull String fileName) {
        Path path = Paths.get(uploadDirectory, fileName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileLoaderIOException("File " + path + " failed while deleting", e);
        }
    }
}

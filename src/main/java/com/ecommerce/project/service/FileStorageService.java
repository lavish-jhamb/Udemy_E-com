package com.ecommerce.project.service;

import com.ecommerce.project.config.FileStorageProperties;
import com.ecommerce.project.payload.response.file.FileUploadResponse;
import com.ecommerce.project.utils.FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Service for handling file storage operations.
 * Validates and stores uploaded files securely.
 */
@Service
@Slf4j
public class FileStorageService {

    private final Path uploadPath;
    private final FileStorageProperties properties;

    public FileStorageService(FileStorageProperties properties) throws IOException {
        this.properties = properties;
        this.uploadPath = Paths.get(properties.getUploadDirectory()).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        log.info("FileStorageService initialized with upload path: {}", uploadPath);
    }

    /**
     * Stores a file after validation.
     *
     * @param file the file to store
     * @return FileUploadResponse containing file details
     * @throws IllegalArgumentException if file validation fails
     */
    public FileUploadResponse storeFile(MultipartFile file) {
        log.info("Starting file upload process for file: {}", file.getOriginalFilename());
        
        // Validate file before storage
        FileValidator.validateFile(file, properties.getMaxFileSize());

        try {
            // Generate unique filename with timestamp to prevent conflicts
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path targetLocation = uploadPath.resolve(filename).normalize();
            
            // Ensure target location is within upload directory (security check)
            if (!targetLocation.getParent().equals(uploadPath)) {
                log.error("Potential path traversal attempt detected for file: {}", filename);
                throw new IllegalArgumentException("Invalid file path");
            }
            
            // Copy file to target location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("File stored successfully: {} at location: {}", filename, targetLocation);
            return new FileUploadResponse(
                    filename,
                    uploadPath.toString(),
                    file.getSize(),
                    file.getContentType()
            );

        } catch (IOException ex) {
            log.error("Error storing file: {}", file.getOriginalFilename(), ex);
            throw new RuntimeException("Could not store file: " + ex.getMessage(), ex);
        }
    }
}


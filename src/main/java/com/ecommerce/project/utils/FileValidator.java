package com.ecommerce.project.utils;

import com.ecommerce.project.exception.custom.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for validating file uploads.
 * Provides validation for file type, size, and other constraints.
 */
@Slf4j
public class FileValidator {

    // Allowed image MIME types
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp",
            "image/bmp"
    ));

    // Allowed file extensions
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp", "bmp"
    ));

    /**
     * Validates if file is null or empty.
     *
     * @param file the file to validate
     * @throws ApiException if file is null or empty
     */
    public static void validateFileNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("File upload attempt with null or empty file");
            throw new ApiException("file cannot be null or empty");
        }
    }

    /**
     * Validates file type/MIME type.
     *
     * @param file the file to validate
     * @throws ApiException if file type is not allowed
     */
    public static void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            log.warn("File upload attempt with invalid MIME type: {}", contentType);
            throw new ApiException("Invalid file type. Only image files (jpeg, jpg, png, gif, webp, bmp) are allowed");
        }
    }

    /**
     * Validates file extension.
     *
     * @param file the file to validate
     * @throws ApiException if file extension is not allowed
     */
    public static void validateFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        
        if (filename == null || !filename.contains(".")) {
            log.warn("File upload attempt with invalid filename: {}", filename);
            throw new ApiException("Invalid filename. File must have an extension");
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            log.warn("File upload attempt with invalid extension: {}", extension);
            throw new ApiException("Invalid file extension. Only image files (jpeg, jpg, png, gif, webp, bmp) are allowed");
        }
    }

    /**
     * Validates file size.
     *
     * @param file the file to validate
     * @param maxSizeInBytes maximum allowed file size in bytes
     * @throws ApiException if file size exceeds maximum
     */
    public static void validateFileSize(MultipartFile file, long maxSizeInBytes) {
        if (file.getSize() > maxSizeInBytes) {
            long maxSizeInMB = maxSizeInBytes / (1024 * 1024);
            log.warn("File upload attempt with size {} bytes exceeding max {} bytes", file.getSize(), maxSizeInBytes);
            throw new ApiException("File size exceeds maximum allowed size of %d MB".formatted(maxSizeInMB));
        }
    }

    /**
     * Validates file name for security issues (path traversal, etc).
     *
     * @param filename the filename to validate
     * @throws ApiException if filename contains invalid characters
     */
    public static void validateFilename(String filename) {
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            log.warn("File upload attempt with potentially unsafe filename: {}", filename);
            throw new ApiException("Invalid filename. Filenames cannot contain path separators or traversal sequences");
        }
    }

    /**
     * Performs all validation checks on the uploaded file.
     *
     * @param file the file to validate
     * @param maxSizeInBytes maximum allowed file size in bytes
     * @throws ApiException if any validation fails
     */
    public static void validateFile(MultipartFile file, long maxSizeInBytes) {
        validateFileNotEmpty(file);
        validateFileType(file);
        validateFileExtension(file);
        validateFileSize(file, maxSizeInBytes);
        validateFilename(file.getOriginalFilename());
        
        log.info("File validation passed for file: {}", file.getOriginalFilename());
    }
}

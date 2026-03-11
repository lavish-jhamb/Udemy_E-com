package com.ecommerce.project.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDirectory;
    private Long maxFileSize = 5242880L; // Default 5MB in bytes

    @PostConstruct
    private void init() {
        log.info("File upload directory path: {}", uploadDirectory);
        log.info("Maximum file size: {} bytes ({} MB)", maxFileSize, maxFileSize / (1024 * 1024));
    }

}


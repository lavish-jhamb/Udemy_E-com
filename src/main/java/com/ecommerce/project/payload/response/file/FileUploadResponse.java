package com.ecommerce.project.payload.response.file;

public record FileUploadResponse(String filename, String fileDownloadURI, long size, String contentType) {};
package com.ecommerce.project.payload.response.dto;

import com.ecommerce.project.model.enums.ApplicationStatus;

import java.time.LocalDateTime;

public record SellerApplicationResponse(
        Long id,
        Integer userId,
        String username,
        String businessName,
        String description,
        ApplicationStatus status,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt,
        String rejectionReason
) {}

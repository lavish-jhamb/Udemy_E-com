package com.ecommerce.project.payload.response.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * NOTE: Initial idea was to introduce record class for data carrier object like response.
 *       but we can not use it right now because no-args constructor and non-private fields
 *       are required by model mapper library to map the objects.
 *       However, Java records do not provide a no-arg constructor, and you cannot add one manually.
 */
@Data
@NoArgsConstructor
public class ProductResponse {
    private Integer Id;
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discount;
    private String slug;
    private BigDecimal specialPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

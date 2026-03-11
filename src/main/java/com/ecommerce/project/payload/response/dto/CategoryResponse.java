package com.ecommerce.project.payload.response.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NOTE: Initial idea was to introduce record class for data carrier object like response.
 *       but we can not use it right now because no-args constructor and non-private fields
 *       are required by model mapper library to map the objects.
 *       However, Java records do not provide a no-arg constructor, and you cannot add one manually.
 */
@Data
@NoArgsConstructor
public class CategoryResponse {
    private Integer id;
    private String name;
}

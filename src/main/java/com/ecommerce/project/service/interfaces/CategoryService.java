package com.ecommerce.project.service.interfaces;

import com.ecommerce.project.payload.request.CategoryRequest;
import com.ecommerce.project.payload.response.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    
    List<CategoryResponse> getCategories(int page, int size, String sortBy, String sortOrder);

    CategoryResponse createCategory(CategoryRequest categoryDTO);

    CategoryResponse deleteCategory(int id);

    CategoryResponse updateCategories(CategoryRequest categoryDTO, int id);

}

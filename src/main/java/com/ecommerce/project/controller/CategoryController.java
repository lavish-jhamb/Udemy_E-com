package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.request.CategoryRequest;
import com.ecommerce.project.payload.response.dto.CategoryResponse;
import com.ecommerce.project.payload.response.common.Success;
import com.ecommerce.project.service.interfaces.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/${api.version}")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<Success<List<CategoryResponse>>> getCategories(
            @RequestParam(name = "page", defaultValue = AppConstants.PAGE_NUMBER, required = false) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.PAGE_SIZE, required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ) {
        log.info("Fetching categories - page: {}, size: {}, sortBy: {}, sortOrder: {}", page, size, sortBy, sortOrder);
        List<CategoryResponse> categories = categoryService.getCategories(page, size, sortBy, sortOrder);
        String message = categories.isEmpty() ? "no categories found" : "categories fetched successfully";
        return Success.of(HttpStatus.OK, message, categories);
    }

    @PostMapping("/categories")
    public ResponseEntity<Success<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Creating category with name: {}", categoryRequest.getName());
        CategoryResponse savedCategory = categoryService.createCategory(categoryRequest);
        return Success.of(HttpStatus.CREATED, "category with name '%s' created successfully".formatted(categoryRequest.getName()), savedCategory);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Success<CategoryResponse>> deleteCategory(@PathVariable @Positive int id) {
        log.info("Deleting category with ID: {}", id);
        CategoryResponse categoryDTO = categoryService.deleteCategory(id);
        return Success.of(HttpStatus.OK, "category deleted successfully with id %s".formatted(id), categoryDTO);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Success<CategoryResponse>> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable @Positive int id) {
        log.info("Updating category with ID: {} and name: {}", id, categoryRequest.getName());
        CategoryResponse categoryDTO = categoryService.updateCategories(categoryRequest, id);
        return Success.of(HttpStatus.OK, "category with id %s updated successfully".formatted(id), categoryDTO);
    }

}

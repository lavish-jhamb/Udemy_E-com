package com.ecommerce.project.service;

import com.ecommerce.project.exception.custom.ApiException;
import com.ecommerce.project.exception.custom.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.request.CategoryRequest;
import com.ecommerce.project.payload.response.dto.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.service.interfaces.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Override
    public List<CategoryResponse> getCategories(int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();

        return categories.stream()
                .map(category -> mapper.map(category, CategoryResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Optional<Category> category = categoryRepository.findByName(categoryRequest.getName());

        if(category.isPresent()){
            throw new ApiException("category with name %s is already exists.".formatted(categoryRequest.getName()));
        }

        Category mappedCategory = mapper.map(categoryRequest, Category.class);
        Category categoryCreated = categoryRepository.save(mappedCategory);

        return mapper.map(categoryCreated, CategoryResponse.class);
    }

    @Transactional
    @Override
    public CategoryResponse deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", "id", id));
        categoryRepository.delete(category);
        return mapper.map(category, CategoryResponse.class);
    }

    @Transactional
    @Override
    public CategoryResponse updateCategories(CategoryRequest categoryRequest, int id) {
        Category categoryFromDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", "id", id));
        categoryFromDB.setName(categoryRequest.getName());

        Category updatedCategory = categoryRepository.save(categoryFromDB);

        return mapper.map(updatedCategory, CategoryResponse.class);
    }

}

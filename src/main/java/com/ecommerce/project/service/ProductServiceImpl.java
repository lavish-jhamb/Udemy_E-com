package com.ecommerce.project.service;

import com.ecommerce.project.exception.custom.ApiException;
import com.ecommerce.project.exception.custom.ResourceNotFoundException;
import com.ecommerce.project.mapper.ProductMapper;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.request.ProductRequest;
import com.ecommerce.project.payload.response.dto.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.service.interfaces.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));


        Optional<Product> product = productRepository.findByName(productRequest.getName());

        if(product.isPresent()) {
            throw new ApiException("product already exist with name %s".formatted(productRequest.getName()));
        }

        Product newProduct = new Product();
        newProduct.setName(productRequest.getName());
        newProduct.setSlug(generateSlug(productRequest.getName()));
        newProduct.setImage("default.png");
        newProduct.setDescription(productRequest.getDescription());
        newProduct.setQuantity(productRequest.getQuantity());
        newProduct.setPrice(productRequest.getPrice());
        newProduct.setDiscount(productRequest.getDiscount());

        BigDecimal price = productRequest.getPrice();
        BigDecimal discount = productRequest.getDiscount();
        BigDecimal specialPrice = price.subtract(
                price.multiply(discount)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        ).setScale(2, RoundingMode.HALF_UP);

        newProduct.setSpecialPrice(specialPrice);
        newProduct.setCategory(category);

        productRepository.save(newProduct);

        return mapper.toDTO(newProduct);
    }

    @Override
    public List<ProductResponse> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.getContent().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

        List<Product> products = productRepository.findByCategoryId(category.getId());

        return products.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ProductResponse> searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByNameLikeIgnoreCase('%' + keyword + '%');
        return products.stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest productDTO, int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", id));

        product.setName(productDTO.getName());
        product.setSlug(generateSlug(productDTO.getName()));
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());

        BigDecimal price = productDTO.getPrice();
        BigDecimal discount = productDTO.getDiscount();
        BigDecimal specialPrice = price.subtract(
                price.multiply(discount)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        ).setScale(2, RoundingMode.HALF_UP);

        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);

        return mapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", id));
        productRepository.delete(product);
        return mapper.toDTO(product);
    }

    @Override
    public ProductResponse getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", id));
        return mapper.toDTO(product);
    }


    private String generateSlug(String name) {
        return name.toLowerCase()
                .replace(" ", "-")
                .replaceAll("[^a-z0-9-]", "");
    }

}

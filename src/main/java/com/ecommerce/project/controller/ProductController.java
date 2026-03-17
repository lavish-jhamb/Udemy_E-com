package com.ecommerce.project.controller;

import com.ecommerce.project.payload.request.ProductRequest;
import com.ecommerce.project.payload.response.common.Success;
import com.ecommerce.project.payload.response.dto.ProductResponse;
import com.ecommerce.project.payload.response.file.FileUploadResponse;
import com.ecommerce.project.service.FileStorageService;
import com.ecommerce.project.service.interfaces.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/${api.version}")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    public ProductController(ProductService productService, FileStorageService fileStorageService) {
        this.productService = productService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/admin/categories/{id}/product")
    public ResponseEntity<Success<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest, @PathVariable int id) {
        ProductResponse savedProduct = productService.createProduct(productRequest, id);
        return Success.of(HttpStatus.CREATED, "product created successfully", savedProduct);
    }

    /**
     * - Spring native built in support for pagination.
     * - It would automatically bind as ( /resource?page=0&size=10&sort=name,asc )
     * - Simpler to use & production ready.
     * - There are two ways to have default values -> 1.) @PageableDefault(), 2.) Global through application properties.
     */
    @GetMapping("/public/products")
    public ResponseEntity<Success<List<ProductResponse>>> getProducts(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        List<ProductResponse> products = productService.getProducts(pageable);
        String message = products.isEmpty() ? "no products found" : "products fetch successfully";
        return Success.of(HttpStatus.OK, message, products);
    }

    @GetMapping("/public/categories/{id}/product")
    public ResponseEntity<Success<List<ProductResponse>>> getProductsByCategory(@PathVariable int id) {
        List<ProductResponse> products = productService.getProductsByCategory(id);
        String message = products.isEmpty() ? "no products found with given category id %s".formatted(id) : "products fetch by category id %s".formatted(id);
        return Success.of(HttpStatus.OK, message, products);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<Success<List<ProductResponse>>> searchProductsByKeyword(@PathVariable String keyword) {
        List<ProductResponse> products = productService.searchProductsByKeyword(keyword);
        String message = products.isEmpty() ? "products not found with given keyword '%s'".formatted(keyword) : "products found with given keyword '%s'".formatted(keyword);
        return Success.of(HttpStatus.OK, message, products);
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<Success<ProductResponse>> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable int id) {
        ProductResponse product = productService.updateProduct(productRequest, id);
        return Success.of(HttpStatus.OK, "product updated successfully with id %s".formatted(id), product);
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<Success<ProductResponse>> deleteProduct(@PathVariable int id) {
        ProductResponse product = productService.deleteProduct(id);
        return Success.of(HttpStatus.OK, "product deleted successfully with id %s".formatted(id), product);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping("/products/{id}/image")
    public ResponseEntity<Success<FileUploadResponse>> uploadProductImage(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        log.info("Starting file upload for product ID: {}", id);
        
        // Validate product exists before uploading
        ProductResponse product = productService.getProductById(id);
        log.info("Product validation passed for ID: {}", id);
        
        // Upload file with built-in validation
        FileUploadResponse fileResponse = fileStorageService.storeFile(file);
        
        log.info("File uploaded successfully for product ID: {}. Filename: {}", id, fileResponse.filename());
        return Success.of(HttpStatus.CREATED, "image has been uploaded successfully for product id %s".formatted(id), fileResponse);
    }

}

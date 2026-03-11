package com.ecommerce.project.service.interfaces;

import com.ecommerce.project.payload.request.ProductRequest;
import com.ecommerce.project.payload.response.dto.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest, int categoryId);

    List<ProductResponse> getProducts(Pageable pageable);

    List<ProductResponse> getProductsByCategory(int categoryId);

    List<ProductResponse> searchProductsByKeyword(String keyword);

    ProductResponse updateProduct(ProductRequest productRequest, int id);

    ProductResponse deleteProduct(int id);

    ProductResponse getProductById(int id);

}

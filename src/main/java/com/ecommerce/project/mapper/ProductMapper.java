package com.ecommerce.project.mapper;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.request.ProductRequest;
import com.ecommerce.project.payload.response.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper mapper;

    public ProductResponse toDTO(Product product) {
        return mapper.map(product, ProductResponse.class);
    }

    public Product toEntity(ProductRequest productRequest) {
        return mapper.map(productRequest, Product.class);
    }

}

package com.ecommerce.project.repository;

import com.ecommerce.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryId(Integer id);

    List<Product> findByNameLikeIgnoreCase(String keyword);

    Optional<Product> findByName(String name);

}
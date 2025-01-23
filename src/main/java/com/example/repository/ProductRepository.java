package com.example.repository;

import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByManufacturerContainingIgnoreCase(String manufacturer);

    Optional<Product> findByNameAndManufacturerAndCategoryId(String name, String manufacturer, UUID categoryId);
}
package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.model.Product;
import com.example.model.Category;

public class ProductMapper {

    public static Product toEntity(ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setManufacturer(productDTO.getManufacturer());

        Category category = new Category();
        category.setId(productDTO.getCategoryId());
        product.setCategory(category);

        return product;
    }

    public static ProductDTO toDTO(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getManufacturer(),
                product.getCategory().getId()
        );
    }
    public static void updateEntity(Product product, ProductDTO productDTO) {
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getManufacturer() != null) {
            product.setManufacturer(productDTO.getManufacturer());
        }
        if (productDTO.getCategoryId() != null) {
            Category category = new Category();
            category.setId(productDTO.getCategoryId());
            product.setCategory(category);
        }
    }
}
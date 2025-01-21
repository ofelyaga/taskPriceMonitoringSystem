package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.model.Category;
import com.example.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product toEntity(ProductDTO productDTO);

    ProductDTO toDTO(Product product);

    void updateEntity(ProductDTO productDTO, @MappingTarget Product product);

    default Category mapCategoryIdToCategory(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}
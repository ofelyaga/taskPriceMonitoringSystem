package com.example.mapper;

import com.example.dto.CategoryDTO;
import com.example.model.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }

    ;

    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    ;

    public static void updateEntity(Category category, CategoryDTO categoryDTO) {
        if (categoryDTO.getName() != null) {
            category.setName(categoryDTO.getName());
        }
    }
}
package com.example.mapper;

import com.example.dto.CategoryDTO;
import com.example.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryDTO categoryDTO);

    CategoryDTO toDTO(Category category);

    void updateEntity(CategoryDTO categoryDTO, @MappingTarget Category category);
}
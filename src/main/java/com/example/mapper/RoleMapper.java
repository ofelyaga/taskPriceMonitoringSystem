package com.example.mapper;

import com.example.dto.PriceDTO;
import com.example.dto.RoleDTO;
import com.example.model.Price;
import com.example.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDTO(Role role);
}

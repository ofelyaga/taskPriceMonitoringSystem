package com.example.mapper;

import com.example.dto.UserDTO;
import com.example.model.Role;
import com.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    void updateEntity(UserDTO userDTO, @MappingTarget User user);
}
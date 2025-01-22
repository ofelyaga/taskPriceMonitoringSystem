package com.example.mapper;

import com.example.dto.RoleDTO;
import com.example.model.Role;

public class RoleMapper {

    public static Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        return role;
    }

    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName()
        );
    }

    public static void updateEntity(Role role, RoleDTO roleDTO) {
        if (roleDTO.getName() != null) {
            role.setName(roleDTO.getName());
        }
    }
}
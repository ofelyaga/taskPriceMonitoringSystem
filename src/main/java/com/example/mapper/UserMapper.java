package com.example.mapper;

import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        Set<Role> roles = userDTO.getRoleIds().stream()
                .map(roleId -> {
                    Role role = new Role();
                    role.setId(roleId);
                    return role;
                })
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getRoles().stream()
                        .map(Role::getId)
                        .collect(Collectors.toSet())
        );
    }

    public static void updateEntity(User user, UserDTO userDTO) {
        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getPhone() != null) {
            user.setPhone(userDTO.getPhone());
        }
        if (userDTO.getRoleIds() != null) {
            Set<Role> roles = userDTO.getRoleIds().stream()
                    .map(roleId -> {
                        Role role = new Role();
                        role.setId(roleId);
                        return role;
                    })
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
    }
}
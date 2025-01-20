package com.example.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Set<UUID> roleIds; // Список ID ролей
}
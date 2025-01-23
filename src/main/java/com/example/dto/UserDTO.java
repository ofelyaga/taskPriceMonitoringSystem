package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Set<UUID> roleIds;


    public UserDTO(UUID id, String firstName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
    }
}
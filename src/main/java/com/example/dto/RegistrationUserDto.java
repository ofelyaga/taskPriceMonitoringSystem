package com.example.dto;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;

    public RegistrationUserDto(String username, String password, String confirmPassword, String mail) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = mail;
    }

    public RegistrationUserDto() {

    }
}

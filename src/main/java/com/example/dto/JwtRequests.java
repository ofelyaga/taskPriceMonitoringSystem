package com.example.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRequests implements Serializable {
    private String username;
    private String password;


    public JwtRequests(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public JwtRequests() {

    }
}

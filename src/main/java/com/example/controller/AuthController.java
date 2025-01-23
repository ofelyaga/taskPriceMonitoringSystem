package com.example.controller;

import lombok.RequiredArgsConstructor;
import com.example.dto.JwtRequests;
import com.example.dto.RegistrationUserDto;
import com.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequests authRequest){
        return authService.createAuthToken(authRequest);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.signUp(registrationUserDto);
    }
}

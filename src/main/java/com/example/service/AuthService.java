package com.example.service;

import lombok.RequiredArgsConstructor;
import com.example.dto.JwtRequests;
import com.example.dto.JwtResponse;
import com.example.dto.UserDTO;
import com.example.exception.AppError;
import com.example.config.JwtCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtCore jwtCore;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserService userService, JwtCore jwtCore) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtCore = jwtCore;
    }
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequests authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e){
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtCore.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    public ResponseEntity<?> signUp(@RequestBody UserDTO registrationUserDto) {
        if(!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existsUserByUserId(UUID.fromString(String.valueOf(registrationUserDto.getId())))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пользователеь с именем " + registrationUserDto.getFirstName() + " уже существует.");
        }

        UserDTO user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok("User"+user +"is created");
    }
}

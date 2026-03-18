package com.satyam.fintrack.controller;

import com.satyam.fintrack.Security.JwtService;
import com.satyam.fintrack.dto.LoginRequest;
import com.satyam.fintrack.dto.UserRegisterRequest;
import com.satyam.fintrack.dto.UserRegisterResponse;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUSer(@Valid @RequestBody UserRegisterRequest request){
        UserRegisterResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.authenticate(request);

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", token
        ));
    }
}

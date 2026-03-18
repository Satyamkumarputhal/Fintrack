package com.satyam.fintrack.service;

import com.satyam.fintrack.dto.LoginRequest;
import com.satyam.fintrack.dto.UserRegisterRequest;
import com.satyam.fintrack.dto.UserRegisterResponse;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.entity.UserRole;
import com.satyam.fintrack.exceptions.DuplicateResourceException;
import com.satyam.fintrack.exceptions.InvalidCredentialsException;
import com.satyam.fintrack.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public  UserRegisterResponse registerUser(UserRegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new DuplicateResourceException("Email already exists");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    private UserRegisterResponse mapToResponse(User savedUser) {
        UserRegisterResponse response = UserRegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .message("User registered successfully")
                .build();
        return response;
    }

    public User authenticate(LoginRequest request) {

        User user = (User) userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return user;
    }
}

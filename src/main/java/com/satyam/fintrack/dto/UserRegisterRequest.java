package com.satyam.fintrack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Size(min = 6)
    private String password;
}

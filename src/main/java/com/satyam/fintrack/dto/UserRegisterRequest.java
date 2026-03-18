package com.satyam.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @Schema(example = "Alex Johnson")
    @NotBlank
    private String name;

    @Schema(example = "alex@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "secret123")
    @Size(min = 6)
    private String password;
}

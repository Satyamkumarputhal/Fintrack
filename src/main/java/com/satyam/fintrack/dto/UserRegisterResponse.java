package com.satyam.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserRegisterResponse {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Alex Johnson")
    private String name;
    @Schema(example = "alex@example.com")
    private String email;
    @Schema(example = "2026-03-18T10:20:13")
    private LocalDateTime createdAt;
    @Schema(example = "User registered successfully")
    private String message;
}

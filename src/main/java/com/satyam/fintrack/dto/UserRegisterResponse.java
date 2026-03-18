package com.satyam.fintrack.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserRegisterResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private String message;
}

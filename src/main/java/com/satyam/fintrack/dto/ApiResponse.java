package com.satyam.fintrack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return new ApiResponse<>(status.value(), data, LocalDateTime.now());
    }
}

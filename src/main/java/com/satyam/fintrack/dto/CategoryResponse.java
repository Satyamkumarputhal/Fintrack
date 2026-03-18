package com.satyam.fintrack.dto;


import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name
) {}

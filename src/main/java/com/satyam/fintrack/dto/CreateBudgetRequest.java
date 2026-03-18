package com.satyam.fintrack.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateBudgetRequest(
        @NotNull
        @Positive
        Long categoryId,
        @NotNull
        @Min(2000)
        @Max(2100)
        Integer year,
        @NotNull
        @Min(1)
        @Max(12)
        Integer month,
        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount
) {}

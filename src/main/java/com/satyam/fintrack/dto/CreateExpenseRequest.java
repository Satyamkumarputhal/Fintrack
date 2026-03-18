package com.satyam.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateExpenseRequest", description = "Payload for creating an expense")
public record CreateExpenseRequest(

        @NotNull
        @DecimalMin(value = "0.01")
        @DecimalMax(value = "999999999.99")
        @Schema(example = "2499.99")
        BigDecimal amount,

        @Size(max = 255)
        @Schema(example = "Groceries")
        String description,

        @NotNull
        @PastOrPresent
        @Schema(example = "2026-03-15")
        LocalDate expenseDate,

        @NotNull
        @Positive
        @Schema(example = "1")
        Long categoryId
) {}

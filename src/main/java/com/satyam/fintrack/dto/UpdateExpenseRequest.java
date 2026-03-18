package com.satyam.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "UpdateExpenseRequest", description = "Payload for updating an expense")
public record UpdateExpenseRequest(

        @Schema(example = "199.99")
        @DecimalMin("0.01")
        BigDecimal amount,
        @Schema(example = "Fuel")
        @Size(max = 255)
        String description,
        @Schema(example = "2026-03-18")
        @PastOrPresent
        LocalDate expenseDate,
        @Schema(example = "2")
        @Positive
        Long categoryId

) {}

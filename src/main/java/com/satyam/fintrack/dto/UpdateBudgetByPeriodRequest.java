package com.satyam.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(name = "UpdateBudgetByPeriodRequest", description = "Payload for updating a budget by category and month")
public record UpdateBudgetByPeriodRequest(
        @Schema(example = "1")
        @NotNull
        @Positive
        Long categoryId,
        @Schema(example = "2026")
        @NotNull
        @Min(2000)
        @Max(2100)
        Integer year,
        @Schema(example = "3")
        @NotNull
        @Min(1)
        @Max(12)
        Integer month,
        @Schema(example = "12000.00")
        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount
) {}

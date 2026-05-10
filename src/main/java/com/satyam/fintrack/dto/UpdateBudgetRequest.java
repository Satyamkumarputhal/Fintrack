package com.satyam.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "UpdateBudgetRequest", description = "Payload for updating a budget amount")
public record UpdateBudgetRequest(
        @Schema(example = "12000.00")
        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount
) {}

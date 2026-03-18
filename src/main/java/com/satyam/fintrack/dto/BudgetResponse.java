package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public record BudgetResponse(
        Long id,
        String category,
        Integer year,
        Integer month,
        BigDecimal budgetAmount
) {}

package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public record CreateBudgetRequest(
        Long categoryId,
        Integer year,
        Integer month,
        BigDecimal amount
) {}

package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public record BudgetSummaryResponse(

        String category,
        BigDecimal budget,
        BigDecimal spent,
        BigDecimal remaining,
        String status

) {}

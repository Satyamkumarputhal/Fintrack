package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public interface BudgetSummaryProjection {
    String getCategoryName();

    BigDecimal getBudgetAmount();

    BigDecimal getSpentAmount();
}

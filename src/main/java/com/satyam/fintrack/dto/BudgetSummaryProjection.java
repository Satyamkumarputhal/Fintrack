package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public interface BudgetSummaryProjection {
    Long getBudgetId();

    Long getCategoryId();

    String getCategoryName();

    BigDecimal getBudgetAmount();

    BigDecimal getSpentAmount();
}

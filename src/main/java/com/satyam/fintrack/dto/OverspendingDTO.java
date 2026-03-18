package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public class OverspendingDTO {

    private String category;
    private BigDecimal budget;
    private BigDecimal spent;

    public OverspendingDTO(String category,
                           BigDecimal budget,
                           BigDecimal spent) {
        this.category = category;
        this.budget = budget;
        this.spent = spent;
    }

    public String getCategory() { return category; }
    public BigDecimal getBudget() { return budget; }
    public BigDecimal getSpent() { return spent; }
}

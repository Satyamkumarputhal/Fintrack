package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public class SpendingHealthDTO {

    private BigDecimal totalBudget;
    private BigDecimal totalSpent;
    private String health;

    public SpendingHealthDTO(BigDecimal totalBudget,
                             BigDecimal totalSpent,
                             String health) {
        this.totalBudget = totalBudget;
        this.totalSpent = totalSpent;
        this.health = health;
    }

    public BigDecimal getTotalBudget() { return totalBudget; }
    public BigDecimal getTotalSpent() { return totalSpent; }
    public String getHealth() { return health; }
}

package com.satyam.fintrack.dto;

import java.math.BigDecimal;

public class SavingsEstimateDTO {

    private BigDecimal incomeEstimate;
    private BigDecimal totalSpent;
    private BigDecimal estimatedSavings;

    public SavingsEstimateDTO(BigDecimal incomeEstimate,
                              BigDecimal totalSpent,
                              BigDecimal estimatedSavings) {
        this.incomeEstimate = incomeEstimate;
        this.totalSpent = totalSpent;
        this.estimatedSavings = estimatedSavings;
    }

    public BigDecimal getIncomeEstimate() { return incomeEstimate; }
    public BigDecimal getTotalSpent() { return totalSpent; }
    public BigDecimal getEstimatedSavings() { return estimatedSavings; }
}

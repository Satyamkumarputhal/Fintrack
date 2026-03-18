package com.satyam.fintrack.dto;

public record BudgetSummary(String category,
                            Double budget,
                            Double spent) {
}

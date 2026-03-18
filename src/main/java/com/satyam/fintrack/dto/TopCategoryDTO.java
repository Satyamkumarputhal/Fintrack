package com.satyam.fintrack.dto;


import java.math.BigDecimal;

public class TopCategoryDTO {

    private String category;
    private BigDecimal amount;

    public TopCategoryDTO(String category, BigDecimal amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
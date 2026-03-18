package com.satyam.fintrack.dto;


import java.math.BigDecimal;

public class CategoryBreakdownDTO {

    private String category;
    private BigDecimal total;

    public CategoryBreakdownDTO(String category, BigDecimal total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotal() {
        return total;
    }
}

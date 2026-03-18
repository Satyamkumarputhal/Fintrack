package com.satyam.fintrack.dto;


import java.math.BigDecimal;

public class MonthlyTrendDTO {

    private Integer month;
    private BigDecimal total;

    public MonthlyTrendDTO(Integer month, BigDecimal total) {
        this.month = month;
        this.total = total;
    }

    public Integer getMonth() {
        return month;
    }

    public BigDecimal getTotal() {
        return total;
    }
}

package com.satyam.fintrack.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseRequest(

        @DecimalMin("0.01")
        BigDecimal amount,
        @Size(max = 255)
        String description,
        @PastOrPresent
        LocalDate expenseDate,
        @Positive
        Long categoryId

) {}

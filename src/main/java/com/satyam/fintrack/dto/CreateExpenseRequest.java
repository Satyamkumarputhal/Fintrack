package com.satyam.fintrack.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record CreateExpenseRequest(

        @NotNull
        @DecimalMin(value = "0.01")
        @DecimalMax(value = "999999999.99")
        BigDecimal amount,

        @Size(max = 255)
        String description,

        @NotNull
        @PastOrPresent
        LocalDate expenseDate,

        @NotNull
        @Positive
        Long categoryId
) {}

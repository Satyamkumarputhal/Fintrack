package com.satyam.fintrack.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseRequest(

        BigDecimal amount,
        String description,
        LocalDate expenseDate,
        Long categoryId

) {}

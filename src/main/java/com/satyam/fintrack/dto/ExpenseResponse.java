package com.satyam.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "ExpenseResponse", description = "Expense response payload")
public record ExpenseResponse(

                @Schema(example = "42")
                Long id,

                @Schema(example = "2499.99")
                BigDecimal amount,

                @Schema(example = "Groceries")
                String description,

                @Schema(example = "2026-03-15")
                LocalDate expenseDate,

                @Schema(example = "1")
                Long categoryId,

                @Schema(example = "food")
                String categoryName

) {
}

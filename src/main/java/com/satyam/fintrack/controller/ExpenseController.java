package com.satyam.fintrack.controller;

import com.satyam.fintrack.dto.ApiResponse;
import com.satyam.fintrack.dto.CreateExpenseRequest;
import com.satyam.fintrack.dto.ExpenseResponse;
import com.satyam.fintrack.dto.PageResponse;
import com.satyam.fintrack.dto.UpdateExpenseRequest;
import com.satyam.fintrack.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@Validated
@Tag(name = "Expenses", description = "Expense CRUD APIs")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Create an expense")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createExpense(
            @Valid @RequestBody CreateExpenseRequest request) {

        expenseService.createExpense(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED, Map.of(
                        "categoryId", request.categoryId(),
                        "expenseDate", request.expenseDate()
                )));
    }

    @GetMapping
    @Operation(summary = "List expenses with pagination and filters")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseResponse>>> getExpenses(

            @RequestParam(defaultValue = "0")
            @Min(0)
            @Schema(example = "0", minimum = "0")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(1)
            @Max(100)
            @Schema(example = "10", minimum = "1", maximum = "100")
            int size,

            @RequestParam(required = false) Long categoryId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate

    ) {

        PageResponse<ExpenseResponse> response =
                expenseService.getExpenses(page, size, categoryId, startDate, endDate);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single expense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpense(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, expenseService.getExpense(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expense")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, Map.of("deletedExpenseId", id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseRequest request
    ) {

        ExpenseResponse response = expenseService.updateExpense(id, request);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, response));
    }
}

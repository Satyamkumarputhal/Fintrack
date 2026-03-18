package com.satyam.fintrack.controller;

import com.satyam.fintrack.dto.ApiResponse;
import com.satyam.fintrack.dto.BudgetSummaryResponse;
import com.satyam.fintrack.dto.CreateBudgetRequest;
import com.satyam.fintrack.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Validated
@Tag(name = "Budgets", description = "Budget management APIs")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @Operation(summary = "Create a budget")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createBudget(
            @Valid @RequestBody CreateBudgetRequest request
    ) {

        budgetService.createBudget(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED, Map.of(
                        "categoryId", request.categoryId(),
                        "year", request.year(),
                        "month", request.month()
                )));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get monthly budget summary")
    public ResponseEntity<ApiResponse<List<BudgetSummaryResponse>>> getBudgetSummary(

            @RequestParam int year,
            @RequestParam int month

    ) {

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                budgetService.getBudgetSummary(year, month)
        ));
    }
}

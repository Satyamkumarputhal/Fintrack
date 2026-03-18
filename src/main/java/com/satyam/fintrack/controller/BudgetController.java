package com.satyam.fintrack.controller;

import com.satyam.fintrack.dto.ApiResponse;
import com.satyam.fintrack.dto.BudgetSummaryResponse;
import com.satyam.fintrack.dto.CreateBudgetRequest;
import com.satyam.fintrack.service.BudgetService;
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
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
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

package com.satyam.fintrack.controller;

import com.satyam.fintrack.dto.BudgetSummaryResponse;
import com.satyam.fintrack.dto.CreateBudgetRequest;
import com.satyam.fintrack.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Void> createBudget(
            @RequestBody CreateBudgetRequest request
    ) {

        budgetService.createBudget(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/summary")
    public List<BudgetSummaryResponse> getBudgetSummary(

            @RequestParam int year,
            @RequestParam int month

    ) {

        return budgetService.getBudgetSummary(year, month);
    }
}

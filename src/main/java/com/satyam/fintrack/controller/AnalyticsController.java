package com.satyam.fintrack.controller;



import com.satyam.fintrack.dto.*;
import com.satyam.fintrack.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "Analytics and reporting APIs")
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/analytics/monthly-total")
    @Operation(summary = "Get monthly total spending")
    public ResponseEntity<ApiResponse<Double>> getMonthlyTotal(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.getMonthlyTotal(year, month)
        ));
    }

    @GetMapping("/category-breakdown")
    @Operation(summary = "Get spending breakdown by category")
    public ResponseEntity<ApiResponse<List<CategoryBreakdownDTO>>> categoryBreakdown(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.categoryBreakdown(year, month)
        ));
    }

    @GetMapping("/top-category")
    @Operation(summary = "Get top spending category for a month")
    public ResponseEntity<ApiResponse<TopCategoryDTO>> topCategory(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.topCategory(year, month)
        ));
    }

    @GetMapping("/monthly-trend")
    @Operation(summary = "Get monthly spending trend")
    public ResponseEntity<ApiResponse<List<MonthlyTrendDTO>>> monthlyTrend(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.monthlyTrend(year)
        ));
    }

    @GetMapping("/spending-health")
    @Operation(summary = "Get spending health indicators")
    public ResponseEntity<ApiResponse<SpendingHealthDTO>> spendingHealth(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.getSpendingHealth(year, month)
        ));
    }

    @GetMapping("/overspending")
    @Operation(summary = "Get overspending categories")
    public ResponseEntity<ApiResponse<List<OverspendingDTO>>> overspending(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.getOverspending(year, month)
        ));
    }

    @GetMapping("/savings-estimate")
    @Operation(summary = "Get savings estimate")
    public ResponseEntity<ApiResponse<SavingsEstimateDTO>> savingsEstimate(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.getSavingsEstimate(year, month)
        ));
    }
}

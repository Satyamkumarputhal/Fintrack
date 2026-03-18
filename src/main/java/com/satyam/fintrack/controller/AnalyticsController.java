package com.satyam.fintrack.controller;



import com.satyam.fintrack.dto.*;
import com.satyam.fintrack.service.AnalyticsService;
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
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/analytics/monthly-total")
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
    public ResponseEntity<ApiResponse<List<MonthlyTrendDTO>>> monthlyTrend(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.monthlyTrend(year)
        ));
    }

    @GetMapping("/spending-health")
    public ResponseEntity<ApiResponse<SpendingHealthDTO>> spendingHealth(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.getSpendingHealth(year, month)
        ));
    }

    @GetMapping("/overspending")
    public ResponseEntity<ApiResponse<List<OverspendingDTO>>> overspending(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.getOverspending(year, month)
        ));
    }

    @GetMapping("/savings-estimate")
    public ResponseEntity<ApiResponse<SavingsEstimateDTO>> savingsEstimate(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                analyticsService.getSavingsEstimate(year, month)
        ));
    }
}

package com.satyam.fintrack.controller;



import com.satyam.fintrack.dto.*;
import com.satyam.fintrack.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
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
    public Double getMonthlyTotal(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return analyticsService.getMonthlyTotal(year, month);
    }

    @GetMapping("/category-breakdown")
    public List<CategoryBreakdownDTO> categoryBreakdown(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return analyticsService.categoryBreakdown(year, month);
    }

    @GetMapping("/top-category")
    public TopCategoryDTO topCategory(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return analyticsService.topCategory(year, month);
    }

    @GetMapping("/monthly-trend")
    public List<MonthlyTrendDTO> monthlyTrend(
            @RequestParam int year
    ) {
        return analyticsService.monthlyTrend(year);
    }

    @GetMapping("/spending-health")
    public SpendingHealthDTO spendingHealth(
            @RequestParam int year,
            @RequestParam int month) {
        return analyticsService.getSpendingHealth(year, month);
    }

    @GetMapping("/overspending")
    public List<OverspendingDTO> overspending(
            @RequestParam int year,
            @RequestParam int month) {
        return analyticsService.getOverspending(year, month);
    }

    @GetMapping("/savings-estimate")
    public SavingsEstimateDTO savingsEstimate(
            @RequestParam int year,
            @RequestParam int month) {
        return analyticsService.getSavingsEstimate(year, month);
    }
}

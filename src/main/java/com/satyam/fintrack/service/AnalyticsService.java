package com.satyam.fintrack.service;

import com.satyam.fintrack.dto.*;
import com.satyam.fintrack.repository.AnalyticsRepository;
import com.satyam.fintrack.repository.BudgetRepository;
import com.satyam.fintrack.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return Long.parseLong(authentication.getName());
    }

    public Double getMonthlyTotal(int year, int month) {

        Long userId = getCurrentUserId();

        Double total = analyticsRepository.getMonthlyTotal(userId, year, month);

        return total != null ? total : 0.0;
    }

    public List<CategoryBreakdownDTO> categoryBreakdown(int year, int month) {

        Long userId = getCurrentUserId();

        return expenseRepository.getCategoryBreakdown(userId, year, month);
    }

    public TopCategoryDTO topCategory(int year, int month) {

        Long userId = getCurrentUserId();

        List<TopCategoryDTO> result =
                expenseRepository.getTopCategory(userId, year, month);

        if(result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    public List<MonthlyTrendDTO> monthlyTrend(int year) {

        Long userId = getCurrentUserId();

        return expenseRepository.getMonthlyTrend(userId, year);
    }

    public SpendingHealthDTO getSpendingHealth(int year, int month) {

        Long userId = getCurrentUserId();

        BigDecimal totalBudget =
                budgetRepository.getTotalBudget(userId, year, month);

        BigDecimal totalSpent =
                BigDecimal.valueOf(analyticsRepository.getMonthlyTotal(userId, year, month));

        if(totalBudget == null) totalBudget = BigDecimal.ZERO;
        if(totalSpent == null) totalSpent = BigDecimal.ZERO;

        double ratio = totalSpent.doubleValue() /
                Math.max(totalBudget.doubleValue(), 1);

        String health;

        if(ratio <= 0.7) {
            health = "GOOD";
        } else if(ratio <= 0.9) {
            health = "WARNING";
        } else {
            health = "CRITICAL";
        }

        return new SpendingHealthDTO(totalBudget, totalSpent, health);
    }

    public List<OverspendingDTO> getOverspending(int year, int month) {

        Long userId = getCurrentUserId();

        List<BudgetSummaryProjection> summary =
                budgetRepository.getBudgetSummary(userId, year, month);

        return summary.stream()
                .filter(s -> s.getSpentAmount().compareTo(s.getBudgetAmount()) > 0)
                .map(s -> new OverspendingDTO(
                        s.getCategoryName(),
                        s.getBudgetAmount(),
                        s.getSpentAmount()
                ))
                .toList();
    }

    public SavingsEstimateDTO getSavingsEstimate(int year, int month) {

        Long userId = getCurrentUserId();

        BigDecimal totalSpent =
                BigDecimal.valueOf(analyticsRepository.getMonthlyTotal(userId, year, month));

        if(totalSpent == null) totalSpent = BigDecimal.ZERO;

        BigDecimal incomeEstimate = new BigDecimal("50000");

        BigDecimal savings = incomeEstimate.subtract(totalSpent);

        return new SavingsEstimateDTO(
                incomeEstimate,
                totalSpent,
                savings
        );
    }
    public BigDecimal getMonthlyTotalForUser(Long userId, int year, int month) {

        Double total = analyticsRepository.getMonthlyTotal(userId, year, month);

        if (total == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(total);
    }

    public TopCategoryDTO getTopCategoryForUser(Long userId, int year, int month) {

        List<TopCategoryDTO> result =
                expenseRepository.getTopCategory(userId, year, month);

        if(result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }
    public SavingsEstimateDTO getSavingsEstimateForUser(Long userId, int year, int month) {

        Double spent = analyticsRepository.getMonthlyTotal(userId, year, month);

        BigDecimal totalSpent =
                spent != null ? BigDecimal.valueOf(spent) : BigDecimal.ZERO;

        BigDecimal incomeEstimate = new BigDecimal("50000");

        BigDecimal savings = incomeEstimate.subtract(totalSpent);

        return new SavingsEstimateDTO(
                incomeEstimate,
                totalSpent,
                savings
        );
    }


}
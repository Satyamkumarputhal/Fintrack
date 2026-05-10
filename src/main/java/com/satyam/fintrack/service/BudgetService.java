package com.satyam.fintrack.service;

import com.satyam.fintrack.Security.SecurityUtils;
import com.satyam.fintrack.dto.BudgetSummaryProjection;
import com.satyam.fintrack.dto.BudgetSummaryResponse;
import com.satyam.fintrack.dto.CreateBudgetRequest;
import com.satyam.fintrack.dto.UpdateBudgetByPeriodRequest;
import com.satyam.fintrack.dto.UpdateBudgetRequest;
import com.satyam.fintrack.entity.Budget;
import com.satyam.fintrack.entity.Category;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.exceptions.BudgetAlreadyExistsException;
import com.satyam.fintrack.exceptions.ResourceNotFoundException;
import com.satyam.fintrack.repository.BudgetRepository;
import com.satyam.fintrack.repository.CategoryRepository;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void createBudget(CreateBudgetRequest request) {

        Long userId = SecurityUtils.getAuthenticatedUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found");
        }

        Optional<Budget> existing =
                budgetRepository.findByUserIdAndCategoryIdAndYearAndMonth(
                        userId,
                        request.categoryId(),
                        request.year(),
                        request.month()
                );

        if (existing.isPresent()) {
            throw new BudgetAlreadyExistsException(
                    "Budget already exists for this category and month"
            );
        }

        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .year(request.year())
                .month(request.month())
                .amount(request.amount())
                .build();

        budgetRepository.save(budget);
    }

    public void updateBudget(Long budgetId, UpdateBudgetRequest request) {
        Long userId = SecurityUtils.getAuthenticatedUserId();

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        if (!budget.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Budget not found");
        }

        budget.setAmount(request.amount());
        budgetRepository.save(budget);
    }

    public void updateBudgetByCategoryAndPeriod(UpdateBudgetByPeriodRequest request) {
        Long userId = SecurityUtils.getAuthenticatedUserId();

        Budget budget = budgetRepository.findByUserIdAndCategoryIdAndYearAndMonth(
                        userId,
                        request.categoryId(),
                        request.year(),
                        request.month()
                )
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        budget.setAmount(request.amount());
        budgetRepository.save(budget);
    }

    public List<BudgetSummaryResponse> getBudgetSummary(int year, int month) {

        Long userId = SecurityUtils.getAuthenticatedUserId();
        return getBudgetSummaryForUser(userId, year, month);
    }

    public List<BudgetSummaryResponse> getBudgetSummaryForUser(
            Long userId,
            int year,
            int month
    ) {

        List<BudgetSummaryProjection> results =
                budgetRepository.getBudgetSummary(userId, year, month);

        return results.stream().map(r -> {

            BigDecimal budget = r.getBudgetAmount();
            BigDecimal spent = r.getSpentAmount();

            BigDecimal remaining = budget.subtract(spent);

            BigDecimal percentage = BigDecimal.ZERO;

            if (budget.compareTo(BigDecimal.ZERO) > 0) {
                percentage = spent
                        .divide(budget, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            String status;

            if (percentage.compareTo(BigDecimal.valueOf(80)) < 0) {
                status = "SAFE";
            } else if (percentage.compareTo(BigDecimal.valueOf(100)) <= 0) {
                status = "WARNING";
            } else {
                status = "EXCEEDED";
            }

            return new BudgetSummaryResponse(
                    r.getBudgetId(),
                    r.getCategoryId(),
                    r.getCategoryName(),
                    budget,
                    spent,
                    remaining,
                    status
            );

        }).toList();
    }
}

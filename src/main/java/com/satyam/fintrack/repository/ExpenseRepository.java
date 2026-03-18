package com.satyam.fintrack.repository;

import com.satyam.fintrack.dto.BudgetSummaryProjection;
import com.satyam.fintrack.dto.CategoryBreakdownDTO;
import com.satyam.fintrack.dto.MonthlyTrendDTO;
import com.satyam.fintrack.dto.TopCategoryDTO;
import com.satyam.fintrack.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUserId(Long userId, Pageable pageable);
    Page<Expense> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId,
            Pageable pageable
    );
    Page<Expense> findByUserIdAndExpenseDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
    Page<Expense> findByUserIdAndCategoryIdAndExpenseDateBetween(
            Long userId,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    boolean existsByCategoryId(Long categoryId);

    @Query("""
SELECT new com.satyam.fintrack.dto.CategoryBreakdownDTO(
    c.name,
    SUM(e.amount)
)
FROM Expense e
JOIN e.category c
WHERE e.user.id = :userId
AND YEAR(e.expenseDate) = :year
AND MONTH(e.expenseDate) = :month
GROUP BY c.name
ORDER BY SUM(e.amount) DESC
""")
    List<CategoryBreakdownDTO> getCategoryBreakdown(
            Long userId,
            int year,
            int month
    );

    @Query("""
SELECT new com.satyam.fintrack.dto.TopCategoryDTO(
    c.name,
    SUM(e.amount)
)
FROM Expense e
JOIN e.category c
WHERE e.user.id = :userId
AND YEAR(e.expenseDate) = :year
AND MONTH(e.expenseDate) = :month
GROUP BY c.name
ORDER BY SUM(e.amount) DESC
""")
    List<TopCategoryDTO> getTopCategory(
            Long userId,
            int year,
            int month
    );

    @Query("""
SELECT new com.satyam.fintrack.dto.MonthlyTrendDTO(
    MONTH(e.expenseDate),
    SUM(e.amount)
)
FROM Expense e
WHERE e.user.id = :userId
AND YEAR(e.expenseDate) = :year
GROUP BY MONTH(e.expenseDate)
ORDER BY MONTH(e.expenseDate)
""")
    List<MonthlyTrendDTO> getMonthlyTrend(
            Long userId,
            int year
    );

    @Query("""
SELECT new com.satyam.fintrack.dto.CategoryBreakdownDTO(
    c.name,
    SUM(e.amount)
)
FROM Expense e
JOIN e.category c
WHERE e.user.id = :userId
AND e.expenseDate >= :startDate
AND e.expenseDate <= :endDate
GROUP BY c.name
""")
    List<CategoryBreakdownDTO> getWeeklyBreakdown(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}

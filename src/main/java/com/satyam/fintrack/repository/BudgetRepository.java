package com.satyam.fintrack.repository;

import com.satyam.fintrack.dto.BudgetSummaryProjection;
import com.satyam.fintrack.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserIdAndCategoryIdAndYearAndMonth(
            Long userId,
            Long categoryId,
            Integer year,
            Integer month
    );

    List<Budget> findByUserIdAndYearAndMonth(
            Long userId,
            Integer year,
            Integer month
    );

    Optional<Budget> findByUserIdAndCategoryNameAndYearAndMonth(
            Long userId,
            String categoryName,
            Integer year,
            Integer month
    );

    @Query("""
    SELECT
        c.name as categoryName,
        b.amount as budgetAmount,
        COALESCE(SUM(e.amount),0) as spentAmount
    FROM Budget b
    JOIN b.category c
    LEFT JOIN Expense e
      ON e.category.id = c.id
      AND e.user.id = :userId
      AND EXTRACT(YEAR FROM e.expenseDate) = :year
      AND EXTRACT(MONTH FROM e.expenseDate) = :month
    WHERE b.user.id = :userId
    AND b.year = :year
    AND b.month = :month
    GROUP BY c.name, b.amount
    """)
    List<BudgetSummaryProjection> getBudgetSummary(
            Long userId,
            int year,
            int month
    );

    @Modifying
    @Query("""
    UPDATE Budget b
    SET b.lastAlertStatus = :status
    WHERE b.user.id = :userId
    AND b.category.name = :category
    AND b.year = :year
    AND b.month = :month
    """)
    void updateLastAlertStatus(Long userId, String category, int year, int month, String status);

    @Query("""
SELECT SUM(b.amount)
FROM Budget b
WHERE b.user.id = :userId
AND b.year = :year
AND b.month = :month
""")
    BigDecimal getTotalBudget(Long userId, int year, int month);
}
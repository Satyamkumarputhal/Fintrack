package com.satyam.fintrack.repository;

import com.satyam.fintrack.dto.CategoryBreakdownDTO;
import com.satyam.fintrack.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Expense,Long> {
    @Query("""
SELECT COALESCE(SUM(e.amount),0)
FROM Expense e
WHERE e.user.id = :userId
AND EXTRACT(YEAR FROM e.expenseDate) = :year
AND EXTRACT(MONTH FROM e.expenseDate) = :month
""")
    Double getMonthlyTotal(Long userId, int year, int month);



}
package com.satyam.fintrack.service;

import com.satyam.fintrack.dto.UpdateExpenseRequest;
import com.satyam.fintrack.entity.Category;
import com.satyam.fintrack.entity.Expense;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.repository.CategoryRepository;
import com.satyam.fintrack.repository.ExpenseRepository;
import com.satyam.fintrack.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateExpenseConvertsOptimisticLockingConflictsToStableMessage() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "1",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

        User user = User.builder().id(1L).build();
        Category category = Category.builder().id(2L).user(user).name("food").build();
        Expense expense = Expense.builder()
                .id(99L)
                .amount(new BigDecimal("100.00"))
                .description("before")
                .expenseDate(LocalDate.of(2026, 3, 10))
                .user(user)
                .category(category)
                .build();

        UpdateExpenseRequest request = new UpdateExpenseRequest(
                new BigDecimal("120.00"),
                "after",
                LocalDate.of(2026, 3, 11),
                2L
        );

        when(expenseRepository.findById(99L)).thenReturn(Optional.of(expense));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(expenseRepository.saveAndFlush(any(Expense.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException("Expense", 99L));

        OptimisticLockingFailureException ex = assertThrows(
                OptimisticLockingFailureException.class,
                () -> expenseService.updateExpense(99L, request)
        );

        assertEquals("Resource was updated by another request. Please retry.", ex.getMessage());
    }
}

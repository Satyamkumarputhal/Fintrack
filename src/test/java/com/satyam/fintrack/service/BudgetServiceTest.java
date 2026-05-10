package com.satyam.fintrack.service;

import com.satyam.fintrack.dto.UpdateBudgetRequest;
import com.satyam.fintrack.dto.UpdateBudgetByPeriodRequest;
import com.satyam.fintrack.entity.Budget;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.exceptions.ResourceNotFoundException;
import com.satyam.fintrack.repository.BudgetRepository;
import com.satyam.fintrack.repository.CategoryRepository;
import com.satyam.fintrack.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateBudgetUpdatesAmountForOwnedBudget() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "1",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

        User user = User.builder().id(1L).build();
        Budget budget = Budget.builder().id(9L).user(user).amount(new BigDecimal("1000.00")).build();

        when(budgetRepository.findById(9L)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        budgetService.updateBudget(9L, new UpdateBudgetRequest(new BigDecimal("1500.00")));

        assertEquals(new BigDecimal("1500.00"), budget.getAmount());
        verify(budgetRepository).save(budget);
    }

    @Test
    void updateBudgetRejectsBudgetOwnedByAnotherUser() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "1",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

        User otherUser = User.builder().id(2L).build();
        Budget budget = Budget.builder().id(9L).user(otherUser).amount(new BigDecimal("1000.00")).build();

        when(budgetRepository.findById(9L)).thenReturn(Optional.of(budget));

        assertThrows(
                ResourceNotFoundException.class,
                () -> budgetService.updateBudget(9L, new UpdateBudgetRequest(new BigDecimal("1500.00")))
        );

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    void updateBudgetByCategoryAndPeriodUpdatesMatchingBudget() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "1",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

        User user = User.builder().id(1L).build();
        Budget budget = Budget.builder().id(9L).user(user).amount(new BigDecimal("1000.00")).build();

        when(budgetRepository.findByUserIdAndCategoryIdAndYearAndMonth(1L, 4L, 2026, 3))
                .thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        budgetService.updateBudgetByCategoryAndPeriod(
                new UpdateBudgetByPeriodRequest(4L, 2026, 3, new BigDecimal("1500.00"))
        );

        assertEquals(new BigDecimal("1500.00"), budget.getAmount());
        verify(budgetRepository).save(budget);
    }
}

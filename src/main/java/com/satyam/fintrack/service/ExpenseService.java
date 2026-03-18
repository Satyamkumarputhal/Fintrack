package com.satyam.fintrack.service;

import com.satyam.fintrack.Security.SecurityUtils;
import com.satyam.fintrack.dto.CreateExpenseRequest;
import com.satyam.fintrack.dto.ExpenseResponse;
import com.satyam.fintrack.dto.PageResponse;
import com.satyam.fintrack.dto.UpdateExpenseRequest;
import com.satyam.fintrack.entity.Category;
import com.satyam.fintrack.entity.Expense;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.exceptions.ResourceNotFoundException;
import com.satyam.fintrack.repository.CategoryRepository;
import com.satyam.fintrack.repository.ExpenseRepository;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
        private final UserRepository userRepository;
        private final CategoryRepository categoryRepository;
        private final ExpenseRepository expenseRepository;

        public void createExpense(CreateExpenseRequest request) {
                Long userId = SecurityUtils.getAuthenticatedUserId();

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Category category = categoryRepository.findById(request.categoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

                if (!category.getUser().getId().equals(userId)) {
                        throw new AccessDeniedException("Category does not belong to user");
                }

                Expense expense = Expense.builder()
                                .amount(request.amount())
                                .description(request.description())
                                .expenseDate(request.expenseDate())
                                .user(user)
                                .category(category)
                                .build();

                expenseRepository.save(expense);
        }

        public PageResponse<ExpenseResponse> getExpenses(
                        int page,
                        int size,
                        Long categoryId,
                        LocalDate startDate,
                        LocalDate endDate) {

                validatePagination(page, size);
                Long userId = SecurityUtils.getAuthenticatedUserId();

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(
                                                Sort.Order.desc("expenseDate"),
                                                Sort.Order.desc("createdAt")));

                Page<Expense> expensePage;

                if (categoryId != null && startDate != null && endDate != null) {

                        expensePage = expenseRepository
                                        .findByUserIdAndCategoryIdAndExpenseDateBetween(
                                                        userId,
                                                        categoryId,
                                                        startDate,
                                                        endDate,
                                                        pageable);

                } else if (categoryId != null) {

                        expensePage = expenseRepository
                                        .findByUserIdAndCategoryId(
                                                        userId,
                                                        categoryId,
                                                        pageable);

                } else if (startDate != null && endDate != null) {

                        expensePage = expenseRepository
                                        .findByUserIdAndExpenseDateBetween(
                                                        userId,
                                                        startDate,
                                                        endDate,
                                                        pageable);

                } else {

                        expensePage = expenseRepository
                                        .findByUserId(
                                                        userId,
                                                        pageable);
                }

                List<ExpenseResponse> expenses = expensePage.getContent()
                                .stream()
                                .map(expense -> new ExpenseResponse(
                                                expense.getId(),
                                                expense.getAmount(),
                                                expense.getDescription(),
                                                expense.getExpenseDate(),
                                                expense.getCategory().getId(),
                                                expense.getCategory().getName()))
                                .toList();

                return new PageResponse<>(
                                expenses,
                                expensePage.getNumber(),
                                expensePage.getSize(),
                                expensePage.getTotalElements(),
                                expensePage.getTotalPages());
        }

        public ExpenseResponse getExpense(Long expenseId) {

                Long userId = SecurityUtils.getAuthenticatedUserId();

                Expense expense = expenseRepository.findById(expenseId)
                                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

                if (!expense.getUser().getId().equals(userId)) {
                        throw new AccessDeniedException("You cannot access this expense");
                }

                return new ExpenseResponse(
                                expense.getId(),
                                expense.getAmount(),
                                expense.getDescription(),
                                expense.getExpenseDate(),
                                expense.getCategory().getId(),
                                expense.getCategory().getName());
        }

        public void deleteExpense(Long expenseId) {

                Long userId = SecurityUtils.getAuthenticatedUserId();

                Expense expense = expenseRepository.findById(expenseId)
                                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

                if (!expense.getUser().getId().equals(userId)) {
                        throw new AccessDeniedException("You cannot delete this expense");
                }

                expenseRepository.delete(expense);
        }

        @Transactional
        public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request) {

                Long userId = SecurityUtils.getAuthenticatedUserId();

                Expense expense = expenseRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

                if (!expense.getUser().getId().equals(userId)) {
                        throw new AccessDeniedException("You cannot modify this expense");
                }

                if (request.amount() != null) {
                        expense.setAmount(request.amount());
                }

                if (request.description() != null) {
                        expense.setDescription(request.description());
                }

                if (request.expenseDate() != null) {
                        expense.setExpenseDate(request.expenseDate());
                }

                if (request.categoryId() != null) {

                        Category category = categoryRepository.findById(request.categoryId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

                        if (!category.getUser().getId().equals(userId)) {
                                throw new AccessDeniedException("Category does not belong to user");
                        }

                        expense.setCategory(category);
                }

                Expense updated;

                try {
                        updated = expenseRepository.saveAndFlush(expense);
                } catch (OptimisticLockingFailureException ex) {
                        throw new OptimisticLockingFailureException(
                                "Resource was updated by another request. Please retry.",
                                ex
                        );
                }

                return new ExpenseResponse(
                                updated.getId(),
                                updated.getAmount(),
                                updated.getDescription(),
                                updated.getExpenseDate(),
                                updated.getCategory().getId(),
                                updated.getCategory().getName());
        }

        private void validatePagination(int page, int size) {
                if (page < 0) {
                        throw new IllegalArgumentException("page must be greater than or equal to 0");
                }

                if (size <= 0 || size > 100) {
                        throw new IllegalArgumentException("size must be between 1 and 100");
                }
        }
}

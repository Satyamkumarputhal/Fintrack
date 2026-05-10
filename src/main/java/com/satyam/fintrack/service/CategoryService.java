package com.satyam.fintrack.service;

import com.satyam.fintrack.Security.SecurityUtils;
import com.satyam.fintrack.dto.CreateCategoryRequest;
import com.satyam.fintrack.dto.CategoryResponse;
import com.satyam.fintrack.entity.Category;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.exceptions.CategoryAlreadyExistsException;
import com.satyam.fintrack.exceptions.CategoryInUseException;
import com.satyam.fintrack.exceptions.ResourceNotFoundException;
import com.satyam.fintrack.repository.CategoryRepository;
import com.satyam.fintrack.repository.ExpenseRepository;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        String normalizedName = request.name().trim().toLowerCase();
        Long userId = SecurityUtils.getAuthenticatedUserId();
        if (categoryRepository.existsByNameAndUserId(normalizedName, userId)) {
            throw new CategoryAlreadyExistsException("Category already exists");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = Category.builder()
                .name(normalizedName)
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return maptoResponse(savedCategory);
    }

    private CategoryResponse maptoResponse(Category savedCategory) {
        CategoryResponse response = CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .build();
        return response;
    }

    @Transactional
    public List<CategoryResponse> getUserCategories() {

        Long userId = SecurityUtils.getAuthenticatedUserId();

        List<Category> categories =
                categoryRepository.findByUserIdOrderByNameAsc(userId);

        return categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CreateCategoryRequest request) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        String normalizedName = request.name().trim().toLowerCase();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found");
        }

        if (categoryRepository.existsByNameAndUserIdAndIdNot(normalizedName, userId, categoryId)) {
            throw new CategoryAlreadyExistsException("Category already exists");
        }

        category.setName(normalizedName);
        Category updatedCategory = categoryRepository.save(category);
        return maptoResponse(updatedCategory);
    }

    public void deleteCategory(Long categoryId) {

        Long userId = SecurityUtils.getAuthenticatedUserId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found");
        }

        if (expenseRepository.existsByCategoryId(categoryId)) {
            throw new CategoryInUseException(
                    "Category cannot be deleted because expenses exist"
            );
        }

        categoryRepository.delete(category);
    }
}


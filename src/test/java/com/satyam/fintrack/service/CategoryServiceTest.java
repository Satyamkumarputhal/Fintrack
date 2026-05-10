package com.satyam.fintrack.service;

import com.satyam.fintrack.dto.CategoryResponse;
import com.satyam.fintrack.dto.CreateCategoryRequest;
import com.satyam.fintrack.entity.Category;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.exceptions.CategoryAlreadyExistsException;
import com.satyam.fintrack.repository.CategoryRepository;
import com.satyam.fintrack.repository.ExpenseRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private CategoryService categoryService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateCategoryRenamesOwnedCategory() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "1",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

        User user = User.builder().id(1L).build();
        Category category = Category.builder().id(10L).name("groceries").user(user).build();

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameAndUserIdAndIdNot("travel", 1L, 10L)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse response = categoryService.updateCategory(10L, new CreateCategoryRequest("Travel"));

        assertEquals(10L, response.id());
        assertEquals("travel", response.name());
        assertEquals("travel", category.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategoryRejectsDuplicateNameForSameUser() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "1",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

        User user = User.builder().id(1L).build();
        Category category = Category.builder().id(10L).name("groceries").user(user).build();

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameAndUserIdAndIdNot("travel", 1L, 10L)).thenReturn(true);

        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.updateCategory(10L, new CreateCategoryRequest("Travel"))
        );

        verify(categoryRepository, never()).save(any(Category.class));
    }
}

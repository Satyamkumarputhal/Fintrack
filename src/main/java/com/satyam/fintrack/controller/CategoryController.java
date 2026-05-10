package com.satyam.fintrack.controller;

import com.satyam.fintrack.dto.ApiResponse;
import com.satyam.fintrack.dto.CreateCategoryRequest;
import com.satyam.fintrack.dto.CategoryResponse;
import com.satyam.fintrack.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(HttpStatus.CREATED, response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {

        List<CategoryResponse> categories =
                categoryService.getUserCategories();

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, categories));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable long id,
            @RequestBody CreateCategoryRequest request
    ) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteCategories(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, Map.of("deletedCategoryId", id)));
    }
}

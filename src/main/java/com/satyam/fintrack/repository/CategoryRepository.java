package com.satyam.fintrack.repository;

import com.satyam.fintrack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndUserId(String name, Long userId);
    List<Category> findByUserIdOrderByNameAsc(Long userId);
}

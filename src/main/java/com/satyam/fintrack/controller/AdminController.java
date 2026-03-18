package com.satyam.fintrack.controller;

import com.satyam.fintrack.dto.ApiResponse;
import com.satyam.fintrack.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/{id}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> promoteUser(@PathVariable Long id) {
        adminService.promoteUser(id);
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK,
                Map.of("promotedUserId", id)
        ));
    }
}

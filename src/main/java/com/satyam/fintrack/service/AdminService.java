package com.satyam.fintrack.service;

import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.entity.UserRole;
import com.satyam.fintrack.exceptions.ResourceNotFoundException;
import com.satyam.fintrack.exceptions.UserAlreadyAdminException;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    @Transactional
    public void promoteUser(Long id) {
        ensureCurrentUserIsAdmin();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == UserRole.ADMIN) {
            throw new UserAlreadyAdminException("User is already ADMIN");
        }

        user.setRole(UserRole.ADMIN);
    }

    private void ensureCurrentUserIsAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
            throw new AccessDeniedException("Admin access required");
        }
    }
}

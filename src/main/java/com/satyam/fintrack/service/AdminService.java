package com.satyam.fintrack.service;

import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.entity.UserRole;
import com.satyam.fintrack.exceptions.ResourceNotFoundException;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    @Transactional
    public void promoteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalStateException("User is already ADMIN");
        }

        user.setRole(UserRole.ADMIN);
    }
}

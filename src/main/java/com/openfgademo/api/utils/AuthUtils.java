package com.openfgademo.api.utils;

import com.openfgademo.api.data.entity.User;
import com.openfgademo.api.data.repo.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Utility class to access current authenticated user information
 */
@Component
public class AuthUtils {

    private final UserRepo userRepo;

    public AuthUtils(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Get the current authenticated user's email
     *
     * @return the email of the authenticated user or null if not authenticated
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return null;
    }

    /**
     * Get the current authenticated user's ID
     *
     * @return the UUID of the authenticated user or null if not authenticated
     */
    public UUID getCurrentUserId() {
        String email = getCurrentUserEmail();
        if (email == null) {
            return null;
        }

        Optional<User> userOpt = userRepo.findByEmail(email);
        return userOpt.map(User::getId).orElse(null);
    }

    /**
     * Get the current authenticated User entity
     *
     * @return Optional containing the User entity if authenticated, empty otherwise
     */
    public Optional<User> getCurrentUser() {
        String email = getCurrentUserEmail();
        if (email == null) {
            return Optional.empty();
        }

        return userRepo.findByEmail(email);
    }

    /**
     * Check if a user is currently authenticated
     *
     * @return true if a user is authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserDetails;
    }
} 
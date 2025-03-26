package com.openfgademo.api.models.dto.auth;

import com.openfgademo.api.data.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request body for user registration")
public class SignUpFormDto {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "User's password", example = "password123")
    private String password;

    /**
     * Converts this DTO to a User entity.
     *
     * @param id             The UUID to use for the user entity
     * @param hashedPassword The hashed password to store in the entity
     * @return A new User entity populated with data from this DTO
     */
    public User toEntity(UUID id, String hashedPassword) {
        return User.builder()
                .id(id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .passwordHash(hashedPassword)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
} 
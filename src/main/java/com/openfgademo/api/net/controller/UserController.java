package com.openfgademo.api.net.controller;

import com.openfgademo.api.models.dto.user.UserDto;
import com.openfgademo.api.services.UserService;
import com.openfgademo.api.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;
    private final AuthUtils authUtils;

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get current user", description = "Retrieves the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current user retrieved",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        String email = authUtils.getCurrentUserEmail();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
} 
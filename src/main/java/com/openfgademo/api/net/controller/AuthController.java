package com.openfgademo.api.net.controller;

import com.openfgademo.api.models.dto.auth.SignInDto;
import com.openfgademo.api.models.dto.auth.SignInFormDto;
import com.openfgademo.api.models.dto.auth.SignUpFormDto;
import com.openfgademo.api.models.dto.user.UserDto;
import com.openfgademo.api.services.AuthService;
import com.openfgademo.api.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;
    private final AuthUtils authUtils;

    @Operation(summary = "SignUp", description = "Creates a new user account and returns authentication details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = SignInDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpFormDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "SignIn", description = "Authenticates a user and returns authentication details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = SignInDto.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    @PostMapping("/signin")
    public ResponseEntity<SignInDto> login(@RequestBody @Valid SignInFormDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Operation(summary = "Get User Roles", description = "Retrieves roles assigned to a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/roles/{userId}")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable String userId) {
        return ResponseEntity.ok(authService.getRolesByUser(userId));
    }

    @Operation(summary = "Get Current User Roles", description = "Retrieves roles assigned to the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping("/roles")
    public ResponseEntity<List<String>> getCurrentUserRoles() {
        String userId = authUtils.getCurrentUserId().toString();
        return ResponseEntity.ok(authService.getRolesByUser(userId));
    }
}
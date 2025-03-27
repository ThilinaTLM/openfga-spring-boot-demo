package com.openfgademo.api.services;

import com.openfgademo.api.data.entity.User;
import com.openfgademo.api.data.repo.UserRepo;
import com.openfgademo.api.models.common.AppException;
import com.openfgademo.api.models.dto.auth.SignInDto;
import com.openfgademo.api.models.dto.auth.SignInFormDto;
import com.openfgademo.api.models.dto.auth.SignUpFormDto;
import com.openfgademo.api.models.dto.user.UserDto;
import com.openfgademo.api.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public UserDto register(SignUpFormDto request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(
                    "User already exists",
                    HttpStatus.BAD_REQUEST);
        }

        var user = request.toEntity(
                UUID.randomUUID(),
                passwordEncoder.encode(request.getPassword()));

        var savedUser = userRepo.save(user);
        return UserDto.fromEntity(savedUser);
    }

    public SignInDto authenticate(SignInFormDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.UNAUTHORIZED));
        String jwtToken = jwtTokenUtil.generateToken(user);

        return SignInDto.builder()
                .token(jwtToken)
                .user(UserDto.fromEntity(user))
                .build();
    }
}

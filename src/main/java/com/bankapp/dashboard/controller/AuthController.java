package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.dto.AuthResponse;
import com.bankapp.dashboard.dto.LoginRequest;
import com.bankapp.dashboard.dto.UserBasicDto;
import com.bankapp.dashboard.model.Role;
import com.bankapp.dashboard.model.Users;
import com.bankapp.dashboard.repository.UserRepository;
import com.bankapp.dashboard.security.JwtService;
import com.bankapp.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    // ===== CUSTOMER REGISTRATION (always role CUSTOMER) =====
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody Users request) {
        // Force CUSTOMER for self-registration, ignore any role from client
        request.setRole(Role.CUSTOMER);

        Users saved = userService.createUserWithDefaults(request);

        String token = jwtService.generateToken(
                saved.getId(),
                saved.getEmail(),
                saved.getRole().name()
        );

        UserBasicDto dto = new UserBasicDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole().name(),
                saved.getAvatarUrl()
        );

        AuthResponse authResponse = new AuthResponse(token, dto);
        ApiResponse<AuthResponse> body =
                new ApiResponse<>("Registration successful", authResponse);

        return ResponseEntity.ok(body);
    }

    // ===== LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {

        Users user = userRepository.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            ApiResponse<AuthResponse> body =
                    new ApiResponse<>("Invalid email or password", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        UserBasicDto userDto = new UserBasicDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getAvatarUrl()
        );

        AuthResponse authResponse = new AuthResponse(token, userDto);
        ApiResponse<AuthResponse> body =
                new ApiResponse<>("Login successful", authResponse);

        return ResponseEntity.ok(body);
    }
}

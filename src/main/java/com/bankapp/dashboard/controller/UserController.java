package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.model.Users;
import com.bankapp.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                new ApiResponse<>("User API is healthy", "OK")
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Users>>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse<>("Users fetched successfully", users)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Users>> createUser(@RequestBody Users user) {
        Users saved = userService.createUser(user);
        return ResponseEntity
                .status(201)
                .body(new ApiResponse<>("User created successfully", saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Users>> getUserById(@PathVariable String id) {
        Users user = userService.getById(id);
        if (user == null) {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<>("User not found", null));
        }
        return ResponseEntity.ok(
                new ApiResponse<>("User fetched successfully", user)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUserById(id);
        if (!deleted) {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<>("User not found", null));
        }
        return ResponseEntity
                .status(204)
                .body(new ApiResponse<>("User deleted successfully", null));
    }

    @DeleteMapping("/by-email/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteUsersByEmail(@PathVariable String email) {
        long deletedCount = userService.deleteAllByEmail(email);
        String msg = "Deleted " + deletedCount + " user(s) with email " + email;
        return ResponseEntity.ok(new ApiResponse<>(msg, null));
    }
}

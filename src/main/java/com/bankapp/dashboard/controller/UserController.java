package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.dto.UserBasicDto;
import com.bankapp.dashboard.model.Role;
import com.bankapp.dashboard.model.Users;
import com.bankapp.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserBasicDto>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users current = (Users) auth.getPrincipal(); // JwtAuthFilter sets principal = Users

        UserBasicDto dto = new UserBasicDto(
                current.getId(),
                current.getName(),
                current.getEmail(),
                current.getRole().name(),
                current.getAvatarUrl()
        );

        return ResponseEntity.ok(
                new ApiResponse<>("Current user fetched successfully", dto)
        );
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<Role[]>> getRoles() {
        Role[] roles = Role.values();
        return ResponseEntity.ok(
                new ApiResponse<>("Roles fetched successfully", roles)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Users>>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse<>("Users fetched successfully", users)
        );
    }

    /**
     * Generic create endpoint.
     * - If role is null, it will default to CUSTOMER and create default accounts.
     * - If role is ADMIN, it will just create an admin with encoded password.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserBasicDto>> createUser(@RequestBody Users user) {
        Users saved = userService.createUserWithDefaults(user);

        UserBasicDto dto = new UserBasicDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole().name(),
                saved.getAvatarUrl()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("User created successfully", dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUserById(id);
        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("User not found", null));
        }
        // 204 should not normally include a body, but you were returning ApiResponse; here is a cleaner 200.
        return ResponseEntity
                .ok(new ApiResponse<>("User deleted successfully", null));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserBasicDto>> updateCurrentUser(@RequestBody Users user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users current = (Users) auth.getPrincipal();

        Users updated = userService.updateUser(current.getId(), user);
        if (updated == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("User not found", null));
        }

        UserBasicDto dto = new UserBasicDto(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                updated.getRole().name(),
                updated.getAvatarUrl()
        );

        return ResponseEntity.ok(
                new ApiResponse<>("User updated successfully", dto)
        );
    }
}

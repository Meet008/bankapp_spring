package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.PasswordResetDTO;
import com.bankapp.dashboard.dto.PasswordResetRequestDTO;
import com.bankapp.dashboard.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService resetService;

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> requestReset(@RequestBody PasswordResetRequestDTO request) {
        String token = resetService.requestPasswordReset(request); // return null or "" if no user

        if (token != null && !token.isBlank()) {
            Map<String, Object> body = Map.of(
                    "success", true,
                    "message", "Password reset token sent",
                    "token", token
            );
            return ResponseEntity.ok(body);
        } else {
            Map<String, Object> body = Map.of(
                    "success", false,
                    "message", "No user found with this email!"
            );
            // 400 is more correct than 200 here
            return ResponseEntity.badRequest().body(body);
        }
    }


    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody PasswordResetDTO dto) {
        resetService.resetPassword(dto);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password has been successfully reset"
        ));
    }

}

package com.bankapp.dashboard.dto;

import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    private String email;
    private String newPassword;
}

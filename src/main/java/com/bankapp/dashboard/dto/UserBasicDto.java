package com.bankapp.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicDto {
    private String id;
    private String name;
    private String email;
    private String role;
    private String avatarUrl;
}

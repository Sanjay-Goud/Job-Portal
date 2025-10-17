package com.sanjay.job_portal.dto;

import com.sanjay.job_portal.entity.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private User.Role role;
    private Long userId;
}
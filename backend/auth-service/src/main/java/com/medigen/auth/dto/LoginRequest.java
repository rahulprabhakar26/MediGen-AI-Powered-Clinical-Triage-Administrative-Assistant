package com.medigen.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginResponse {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String specialization;
}

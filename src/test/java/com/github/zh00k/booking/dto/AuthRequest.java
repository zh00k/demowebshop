package com.github.zh00k.booking.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}

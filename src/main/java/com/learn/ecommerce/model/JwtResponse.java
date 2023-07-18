package com.learn.ecommerce.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;

    public JwtResponse(String token, String refreshToken, String username, String email, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}

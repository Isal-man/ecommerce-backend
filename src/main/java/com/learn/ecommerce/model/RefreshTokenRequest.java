package com.learn.ecommerce.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RefreshTokenRequest implements Serializable {
    private String refreshToken;
}

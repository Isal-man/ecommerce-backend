package com.learn.ecommerce.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasketRequest implements Serializable {

    private String productId;
    private Double quantity;

}

package com.learn.ecommerce.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest implements Serializable {
    private BigDecimal shipCost;
    private String shipAddress;
    private List<BasketRequest> items;
}

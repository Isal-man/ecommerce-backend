package com.learn.ecommerce.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
public class Product implements Serializable {
    @Id
    private String id;
    private String name;
    private String description;
    private String picture;
    private BigDecimal price;
    private Double stock;

    @JoinColumn
    @ManyToOne
    private Category category;
}

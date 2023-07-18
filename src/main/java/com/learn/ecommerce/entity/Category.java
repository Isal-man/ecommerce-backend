package com.learn.ecommerce.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Category implements Serializable {

    @Id
    private String id;

    private String name;

}

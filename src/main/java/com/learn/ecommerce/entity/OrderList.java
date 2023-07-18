package com.learn.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learn.ecommerce.model.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class OrderList implements Serializable {
    @Id
    private String id;
    private String number;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tanggal;
    @JoinColumn
    @ManyToOne
    private Users users;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderTime;
    private String shipAddress;
    private BigDecimal amount;
    private BigDecimal shipCost;
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}

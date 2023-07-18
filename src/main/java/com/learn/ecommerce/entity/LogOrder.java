package com.learn.ecommerce.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class LogOrder implements Serializable {
    @Id
    private String id;
    private Integer logType;
    private String logMessage;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @JoinColumn
    @ManyToOne
    private OrderList orderList;

    @JoinColumn
    @ManyToOne
    private Users users;

}

package com.learn.ecommerce.service;

import com.learn.ecommerce.entity.LogOrder;
import com.learn.ecommerce.entity.OrderList;
import com.learn.ecommerce.entity.Users;
import com.learn.ecommerce.repository.LogOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class LogOrderService {

    @Autowired
    private LogOrderRepository logOrderRepository;

    public final static Integer DRAFT = 0;
    public final static Integer PEMBAYARAN = 10;
    public final static Integer PACKING = 20;
    public final static Integer PENGIRIMAN = 30;
    public final static Integer SELESAI = 40;
    public final static Integer DIBATALKAN = 100;

    public void insert(String username, OrderList orderList, Integer type, String message) {
        LogOrder logOrder = new LogOrder();
        logOrder.setId(UUID.randomUUID().toString());
        logOrder.setLogMessage(message);
        logOrder.setLogType(type);
        logOrder.setOrderList(orderList);
        logOrder.setUsers(new Users(username));
        logOrder.setTime(new Date());
        logOrderRepository.save(logOrder);
    }

}

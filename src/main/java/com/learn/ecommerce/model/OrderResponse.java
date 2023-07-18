package com.learn.ecommerce.model;

import com.learn.ecommerce.entity.OrderItem;
import com.learn.ecommerce.entity.OrderList;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse implements Serializable {
    private String id;
    private String orderNumber;
    private Date date;
    private String customerName;
    private String shipAddress;
    private Date orderTime;
    private BigDecimal amount;
    private BigDecimal shipCost;
    private BigDecimal total;
    private List<OrderResponse.Item> items;

    public OrderResponse(OrderList orderList, List<OrderItem> orderItems) {
        this.id = orderList.getId();
        this.orderNumber = orderList.getNumber();
        this.date = orderList.getTanggal();
        this.customerName = orderList.getUsers().getName();
        this.shipAddress = orderList.getShipAddress();
        this.orderTime = orderList.getOrderTime();
        this.amount = orderList.getAmount();
        this.shipCost = orderList.getShipCost();
        this.total = orderList.getTotal();
        items = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Item item = new Item();
            item.setProductId(orderItem.getProduct().getId());
            item.setProductName(orderItem.getDescription());
            item.setQuantity(orderItem.getQuantity());
            item.setPrice(orderItem.getPrice());
            item.setAmount(orderItem.getAmount());
            items.add(item);
        }
    }

    @Data
    public static class Item implements Serializable {
        private String productId;
        private String productName;
        private Double quantity;
        private BigDecimal price;
        private BigDecimal amount;
    }
}

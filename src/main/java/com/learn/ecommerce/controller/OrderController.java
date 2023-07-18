package com.learn.ecommerce.controller;

import com.learn.ecommerce.entity.OrderList;
import com.learn.ecommerce.model.OrderRequest;
import com.learn.ecommerce.model.OrderResponse;
import com.learn.ecommerce.security.service.UsersDetailsImpl;
import com.learn.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('user')")
    public OrderResponse makeOrder(@AuthenticationPrincipal UsersDetailsImpl user, @RequestBody OrderRequest request) {
        return orderService.makeOrder(user.getUsername(), request);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasAuthority('user')")
    public OrderList cancelUserOrder(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return orderService.cancelOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/receive")
    @PreAuthorize("hasAuthority('user')")
    public OrderList receiveUserOrder(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return orderService.receiveOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/confirmation")
    @PreAuthorize("hasAuthority('admin')")
    public OrderList confirmationUserOrder(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return orderService.paymentConfirmation(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/packing")
    @PreAuthorize("hasAuthority('admin')")
    public OrderList packingUserOrder(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return orderService.packing(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/send")
    @PreAuthorize("hasAuthority('admin')")
    public OrderList sendUserOrder(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("orderId") String orderId) {
        return orderService.send(orderId, user.getUsername());
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('user')")
    public List<OrderList> findAllOrderUser(
            @AuthenticationPrincipal UsersDetailsImpl user,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "limit", defaultValue = "20", required = false) Integer limit
    ) {
        return orderService.findAllUserOrder(user.getUsername(), page, limit);
    }

    @GetMapping("/orders/admin")
    @PreAuthorize("hasAuthority('admin')")
    public List<OrderList> findAllOrder(
            @AuthenticationPrincipal UsersDetailsImpl user,
            @RequestParam(name = "filterText", defaultValue = "0", required = false) String filterText,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "limit", defaultValue = "20", required = false) Integer limit
    ) {
        return orderService.findAllOrder(filterText, page, limit);
    }

}

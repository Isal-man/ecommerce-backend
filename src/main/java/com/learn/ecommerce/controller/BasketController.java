package com.learn.ecommerce.controller;

import com.learn.ecommerce.entity.Basket;
import com.learn.ecommerce.model.BasketRequest;
import com.learn.ecommerce.security.service.UsersDetailsImpl;
import com.learn.ecommerce.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping("/baskets")
    public List<Basket> findByUserId(@AuthenticationPrincipal UsersDetailsImpl user) {
        return basketService.findByUsersId(user.getUsername());
    }

    @PostMapping("/baskets")
    public Basket insert(@AuthenticationPrincipal UsersDetailsImpl user, @RequestBody BasketRequest request) {
        return basketService.insert(user.getUsername(), request.getProductId(), request.getQuantity());
    }

    @PatchMapping("/baskets/{productId}")
    public Basket update(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("productId") String productId,
                         @RequestParam("quantity") Double quantity) {
        return basketService.update(user.getUsername(), productId, quantity);
    }

    @DeleteMapping("/baskets/{productId}")
    public void delete(@AuthenticationPrincipal UsersDetailsImpl user, @PathVariable("productId") String productId) {
        basketService.delete(user.getUsername(), productId);
    }

}

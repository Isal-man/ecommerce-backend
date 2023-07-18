package com.learn.ecommerce.repository;

import com.learn.ecommerce.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, String> {
    List<Basket> findByUsersId(String username);

    Optional<Basket> findByUsersIdAndProductId(String username, String productId);
}

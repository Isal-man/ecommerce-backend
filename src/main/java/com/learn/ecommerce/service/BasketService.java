package com.learn.ecommerce.service;

import com.learn.ecommerce.entity.Basket;
import com.learn.ecommerce.entity.Product;
import com.learn.ecommerce.entity.Users;
import com.learn.ecommerce.exception.BadRequestException;
import com.learn.ecommerce.repository.BasketRepository;
import com.learn.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasketService {

    private ProductRepository productRepository;
    private BasketRepository basketRepository;

    @Autowired
    public BasketService(ProductRepository productRepository, BasketRepository basketRepository) {
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
    }

    @Transactional
    public Basket insert(String username, String productId, Double quantity) {
//        Check
//        apakah produk yang dipilih ada dalam database
//        Apakah user sudah memiliki keranjang atau belum
//        jika sudah ada maka tambahkan kuantitas pada keranjang user dan hitung jumlahnya
//        jika tidak ada buat keranjang baru

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Produk ID " + productId + " tidak ditemukan"));

        Optional<Basket> optional = basketRepository.findByUsersIdAndProductId(username, productId);
        Basket basket;
        if (optional.isPresent()) {
            basket = optional.get();
            basket.setQuantity(basket.getQuantity() + quantity);
            basket.setAmount(new BigDecimal(basket.getPrice().doubleValue() + basket.getQuantity()));
            basketRepository.save(basket);
        } else {
            basket = new Basket();
            basket.setId(UUID.randomUUID().toString());
            basket.setProduct(product);
            basket.setQuantity(quantity);
            basket.setPrice(product.getPrice());
            basket.setAmount(new BigDecimal(basket.getPrice().doubleValue() * basket.getQuantity()));
            basket.setUsers(new Users(username));
            basketRepository.save(basket);
        }

        return basket;
    }

    @Transactional
    public Basket update(String username, String productId, Double quantity) {
        Basket basket = basketRepository.findByUsersIdAndProductId(username, productId)
                .orElseThrow(() -> new BadRequestException("Produk ID " + productId + " tidak ditemukan"));
        basket.setQuantity(quantity);
        basket.setAmount(new BigDecimal(basket.getPrice().doubleValue() * basket.getQuantity()));
        basketRepository.save(basket);
        return basket;
    }

    @Transactional
    public void delete(String username, String productId) {
        Basket basket = basketRepository.findByUsersIdAndProductId(username, productId)
                .orElseThrow(() -> new BadRequestException("Produk ID " + productId + " tidak ditemukan"));

        basketRepository.delete(basket);
    }

    public List<Basket> findByUsersId(String username) {
        return basketRepository.findByUsersId(username);
    }
}

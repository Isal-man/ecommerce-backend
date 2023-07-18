package com.learn.ecommerce.controller;

import com.learn.ecommerce.entity.Product;
import com.learn.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product findById(@PathVariable("id") String id) {
        return productService.findById(id);
    }

    @PostMapping("/products")
    public Product insert(@RequestBody Product product) {
        return productService.insert(product);
    }

    @PutMapping("/products")
    public Product update(@RequestBody Product product) {
        return productService.update(product);
    }

    @DeleteMapping("/products/{id}")
    public void delete(@PathVariable("id") String id) {
        productService.delete(id);
    }

}

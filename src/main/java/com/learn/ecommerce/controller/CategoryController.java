package com.learn.ecommerce.controller;

import com.learn.ecommerce.entity.Category;
import com.learn.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    Get all data of category
    @GetMapping("/categories")
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/categories/{id}")
    public Category findById(@PathVariable("id") String id) {
        return categoryService.findById(id);
    }

    @PostMapping("/categories")
    public Category insert(@RequestBody Category category) {
        return categoryService.insert(category);
    }

    @PutMapping("/categories")
    public Category update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    @DeleteMapping("/categories/{id}")
    public void delete(@PathVariable("id") String id) {
        categoryService.delete(id);
    }

}

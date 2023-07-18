package com.learn.ecommerce.service;

import com.learn.ecommerce.entity.Category;
import com.learn.ecommerce.exception.ResourceNotFoundException;
import com.learn.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    Cari data kategori berdasarkan ID
    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori dengan id " + id + " tidak ditemukan"));
    }

//    Mendapatkan semua data kategori
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

//    Menambahkan data kategori
    public Category insert(Category category) {
        category.setId(UUID.randomUUID().toString());
        return categoryRepository.save(category);
    }

//    Mengubah data kategori
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

//    Menghapus data kategori
    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}

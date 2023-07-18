package com.learn.ecommerce.service;

import com.learn.ecommerce.entity.Product;
import com.learn.ecommerce.exception.BadRequestException;
import com.learn.ecommerce.exception.ResourceNotFoundException;
import com.learn.ecommerce.repository.CategoryRepository;
import com.learn.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


//    Mendapatkan semua data produk
    public List<Product> findAll() {
        return productRepository.findAll();
    }

//    Mendapatkan data produk berdasarkan ID
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk dengan id " + id + " tidak ditemukan"));
    }

//    Menambahkan data produk
    public Product insert(Product product) {
        if (!StringUtils.hasText(product.getName()))
            throw new BadRequestException("Nama produk tidak boleh kosong");

        if (product.getCategory() == null)
            throw new BadRequestException("Kategori tidak boleh kosong");

        if (!StringUtils.hasText(product.getCategory().getId()))
            throw new BadRequestException("Kategori ID tidak boleh kosong");

        categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new BadRequestException("Kategori dengan ID " + product.getCategory().getId() + " tidak ditemukan"));

        product.setId(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

//    Mengubah data produk
    public Product update(Product product) {
        if (!StringUtils.hasText(product.getId()))
            throw new BadRequestException("Product ID tidak boleh kosong");

        if (!StringUtils.hasText(product.getName()))
            throw new BadRequestException("Nama produk tidak boleh kosong");

        if (product.getCategory() == null)
            throw new BadRequestException("Kategori tidak boleh kosong");

        if (!StringUtils.hasText(product.getCategory().getId()))
            throw new BadRequestException("Kategori ID tidak boleh kosong");

        categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new BadRequestException("Kategori dengan ID " + product.getCategory().getId() + " tidak ditemukan"));
        return productRepository.save(product);
    }

//    Mengubah gambar data produk
    public Product updatePicture(String id, String picture) {
        Product product = findById(id);
        product.setPicture(picture);
        return productRepository.save(product);
    }

//    Menghapus data produk
    public void delete(String id) {
        productRepository.deleteById(id);
    }

}

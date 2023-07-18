package com.learn.ecommerce.service;

import com.learn.ecommerce.entity.Users;
import com.learn.ecommerce.exception.BadRequestException;
import com.learn.ecommerce.exception.ResourceNotFoundException;
import com.learn.ecommerce.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

//    Cari data kategori berdasarkan ID
    public Users findById(String id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan id " + id + " tidak ditemukan"));
    }

//    Mendapatkan semua data kategori
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

//    Menambahkan data kategori
    public Users insert(Users users) {
        if (!StringUtils.hasText(users.getId()))
            throw new BadRequestException("Username harus diisi");

        if (usersRepository.existsById(users.getId()))
            throw new BadRequestException("Username " + users.getId() + " sudah terdaftar");

        if (!StringUtils.hasText(users.getEmail()))
            throw new BadRequestException("Email harus diisi");

        if (usersRepository.existsByEmail(users.getEmail()))
            throw new BadRequestException("Username harus diisi");

        return usersRepository.save(users);
    }

//    Mengubah data kategori
    public Users update(Users users) {
        if (!StringUtils.hasText(users.getId()))
            throw new BadRequestException("Username harus diisi");

        if (!StringUtils.hasText(users.getEmail()))
            throw new BadRequestException("Email harus diisi");

        return usersRepository.save(users);
    }

//    Menghapus data kategori
    public void delete(String id) {
        usersRepository.deleteById(id);
    }
}

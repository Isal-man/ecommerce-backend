package com.learn.ecommerce.controller;

import com.learn.ecommerce.entity.Users;
import com.learn.ecommerce.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/users")
    public List<Users> findAll() {
        return usersService.findAll();
    }

    @GetMapping("/users/{id}")
    public Users findById(@PathVariable("id") String id) {
        return usersService.findById(id);
    }

    @PostMapping("/users")
    public Users insert(@RequestBody Users users) {
        return usersService.insert(users);
    }

    @PutMapping("/users")
    public Users update(@RequestBody Users users) {
        return usersService.update(users);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable("id") String id) {
        usersService.delete(id);
    }

}

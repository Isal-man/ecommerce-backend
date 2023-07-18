package com.learn.ecommerce.repository;

import com.learn.ecommerce.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
    Boolean existsByEmail(String email);
}

package com.learn.ecommerce.repository;

import com.learn.ecommerce.entity.LogOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogOrderRepository extends JpaRepository<LogOrder, String> {
}

package com.learn.ecommerce.repository;

import com.learn.ecommerce.entity.OrderList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderList, String> {
    List<OrderList> findByUsersId(String userId, Pageable pageable);

    @Query("SELECT o from OrderList o where lower(o.number) like %:filterText% or lower(o.users) like %:filterText%")
    List<OrderList> search(@Param("filterText") String filterText, Pageable pageable);
}

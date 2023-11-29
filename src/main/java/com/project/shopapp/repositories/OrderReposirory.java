package com.project.shopapp.repositories;

import com.project.shopapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderReposirory extends JpaRepository<Order, Long> {
   //tìm các đơn của 1 user
   List<Order> findByUserId(Long userId);
}

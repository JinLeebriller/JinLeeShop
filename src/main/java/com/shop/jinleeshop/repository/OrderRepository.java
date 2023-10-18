package com.shop.jinleeshop.repository;

import com.shop.jinleeshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

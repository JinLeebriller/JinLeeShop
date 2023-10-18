package com.shop.jinleeshop.repository;

import com.shop.jinleeshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}

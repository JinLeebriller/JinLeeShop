package com.shop.jinleeshop.repository;

import com.shop.jinleeshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

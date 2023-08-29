package com.foot.repository;

import com.foot.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findByUserId(Long id);
}

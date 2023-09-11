package com.foot.repository;

import com.foot.entity.BidProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidProductRepository extends JpaRepository<BidProduct, Long> {
    List<BidProduct> findByStatus(int status);
}

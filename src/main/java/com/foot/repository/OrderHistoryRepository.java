package com.foot.repository;

import com.foot.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory , Long> {
}

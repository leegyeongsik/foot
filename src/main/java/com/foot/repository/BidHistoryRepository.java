package com.foot.repository;

import com.foot.entity.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long> {
}

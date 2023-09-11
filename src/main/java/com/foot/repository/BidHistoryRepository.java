package com.foot.repository;

import com.foot.dto.bidProduct.BidHistoryChartData;
import com.foot.entity.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long> {
    @Query("SELECT new com.foot.dto.bidProduct.BidHistoryChartData(bh.createdAt, COUNT(bh)) " +
            "FROM BidHistory bh " +
            "WHERE bh.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY bh.createdAt " +
            "ORDER BY bh.createdAt ASC")
    List<BidHistoryChartData> getChartDataByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}



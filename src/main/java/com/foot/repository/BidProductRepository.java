package com.foot.repository;

import com.foot.dto.bidProduct.BidProductChartData;
import com.foot.entity.BidProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BidProductRepository extends JpaRepository<BidProduct, Long> {
    List<BidProduct> findByStatus(int status);


    @Query("SELECT NEW com.foot.dto.bidProduct.BidProductChartData(bp.createdAt, COUNT(bp)) " +
            "FROM BidProduct bp " +
            "WHERE bp.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY bp.createdAt " +
            "ORDER BY bp.createdAt ASC")
    List<BidProductChartData> getChartDataByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}

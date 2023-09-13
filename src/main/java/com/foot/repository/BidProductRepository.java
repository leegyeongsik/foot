package com.foot.repository;

import com.foot.dto.bidProduct.BidProductChartData;
import com.foot.dto.bidProduct.BrandBidProductCount;
import com.foot.entity.BidProduct;
import com.foot.entity.Brand;
import com.foot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BidProductRepository extends JpaRepository<BidProduct, Long> {

    // 경매 상품 마감 여부로 조회
    List<BidProduct> findByStatus(int status);


    // 특정 기간에 등록된 경매 상품 건수 조회
    @Query("SELECT NEW com.foot.dto.bidProduct.BidProductChartData(bp.createdAt, COUNT(bp)) " +
            "FROM BidProduct bp " +
            "WHERE bp.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY bp.createdAt " +
            "ORDER BY bp.createdAt ASC")
    List<BidProductChartData> getChartDataByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 브랜드 이름 별 경매 상품 조회
    @Query("SELECT new com.foot.dto.bidProduct.BrandBidProductCount(bp.brand.name, COUNT(bp)) " +
            "FROM BidProduct bp " +
            "GROUP BY bp.brand.name")
    List<BrandBidProductCount> getBrandBidProductCounts();


    // 경매 상품 이름으로 조회
    Page<BidProduct> findByNameContaining(String searchKeyword, Pageable pageable);


    List<BidProduct> findByStatusAndBrand(int status, Brand brand);

    List<BidProduct> findByUser(User user);

    // 유저가 입찰한 경매 상품 조회
    List<BidProduct> findByBidsUser(User user);
}

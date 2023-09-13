package com.foot.repository;

import com.foot.entity.Bid;
import com.foot.entity.BidProduct;
import com.foot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByUser(User user);

    // 유저의 입찰 최고 금액을 경매 상품 별로 조회
    @Query("SELECT MAX(b.bidPrice) FROM Bid b WHERE b.user = :user AND b.bidProduct = :bidProduct")
    Long findMaxBidPriceByUserAndBidProduct(@Param("user") User user, @Param("bidProduct") BidProduct bidProduct);

}

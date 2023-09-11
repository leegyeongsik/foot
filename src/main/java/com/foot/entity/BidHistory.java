package com.foot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bidHistories")
public class BidHistory extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bid_product_id")
    private BidProduct bidProduct;

    @OneToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    @ManyToOne
    @JoinColumn(name = "sellUserId")
    private User sellUser;

    @ManyToOne
    @JoinColumn(name = "buyUserId")
    private User buyUser;

    public BidHistory(BidProduct bidProduct, Bid bid, User sellUser, User buyUser) {
        this.bidProduct= bidProduct;
        this.bid = bid;
        this.sellUser = sellUser;
        this.buyUser = buyUser;
    }

    public BidHistory(BidProduct bidProduct) {
        this.bidProduct= bidProduct;
    }
}

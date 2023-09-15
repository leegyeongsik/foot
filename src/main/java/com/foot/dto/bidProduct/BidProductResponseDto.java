package com.foot.dto.bidProduct;

import com.foot.entity.Bid;
import com.foot.entity.BidProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class BidProductResponseDto {

    private Long id;
    private String author;
    private String name;
    private String description;
    private Long startPrice;
    private LocalDateTime expirationPeriod;
    private String remainingTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long feetSize;
    private Long footSize;
    private String footPicture;

    private int status;

    private BrandResponseDto brand;

    private Bid topBid;

    private List<BidResponseDto> bidResponseDtoList = new ArrayList<>();

    public BidProductResponseDto(BidProduct bidProduct) {
        this.id = bidProduct.getId();
        this.name = bidProduct.getName();
        this.author = bidProduct.getUser().getName();
        this.description = bidProduct.getDescription();
        this.startPrice = bidProduct.getStartPrice();
        this.expirationPeriod = bidProduct.getExpirationPeriod();
        this.feetSize = bidProduct.getFeetsize();
        this.footSize = bidProduct.getFootsize();
        this.footPicture = bidProduct.getFootpicture();
        this.brand = new BrandResponseDto(bidProduct.getBrand());

        // topBid 설정
        if (bidProduct.getTopBid() != null) {
            this.topBid = new Bid(bidProduct.getTopBid());
        } else {
            // 입찰자가 아무도 없을 때 topBid의 가격을 경매 시작가로 설정
            this.topBid = new Bid();
            this.topBid.setBidPrice(bidProduct.getStartPrice());
        }

        this.status = bidProduct.getStatus();

        this.createdAt = bidProduct.getCreatedAt();
        this.updatedAt = bidProduct.getUpdatedAt();

        this.bidResponseDtoList = bidProduct.getBids().stream()
                .map(BidResponseDto::new)
                .sorted(Comparator.comparing(BidResponseDto::getBidPrice))
                .toList();

    }

    public BidProductResponseDto(BidProduct bidProduct, String remainingTime) {
        this.id = bidProduct.getId();
        this.name = bidProduct.getName();
        this.author = bidProduct.getUser().getName();
        this.description = bidProduct.getDescription();
        this.startPrice = bidProduct.getStartPrice();
        this.expirationPeriod = bidProduct.getExpirationPeriod();
        this.feetSize = bidProduct.getFeetsize();
        this.footSize = bidProduct.getFootsize();
        this.footPicture = bidProduct.getFootpicture();
        this.brand = new BrandResponseDto(bidProduct.getBrand());

        // 입찰자가 아무도 없을 때 topBid의 가격을 경매 시작가로 설정
        if (bidProduct.getTopBid() != null) {
            this.topBid = new Bid(bidProduct.getTopBid());
        } else {
            this.topBid = new Bid();
            this.topBid.setBidPrice(bidProduct.getStartPrice()); // 현재 최고 제시가 0원으로 설정
        }

        this.status = bidProduct.getStatus();

        this.createdAt = bidProduct.getCreatedAt();
        this.updatedAt = bidProduct.getUpdatedAt();
        this.remainingTime = remainingTime;

        this.bidResponseDtoList = bidProduct.getBids().stream()
                .map(BidResponseDto::new)
                .sorted(Comparator.comparing(BidResponseDto::getBidPrice))
                .toList();

    }
}

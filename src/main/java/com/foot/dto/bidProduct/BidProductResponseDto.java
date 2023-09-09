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

    //UserResponseDto

    private Long id;
    private String author;
    private String name;
    private String description;
    private Long startPrice;
    private LocalDateTime expirationPeriod;

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

//        // topBid 설정 (null 대신 빈 객체 생성)
//        if (bidProduct.getTopBid() != null) {
//            this.topBid = new BidResponseDto(bidProduct.getTopBid());
//        } else {
//            this.topBid = new BidResponseDto();
//        }
        // topBid 설정 (경매 제시가가 아직 없을 경우 처리)
        if (bidProduct.getTopBid() != null) {
            this.topBid = bidProduct.getTopBid();
        } else {
            // topBid가 없을 때 topBid 제시자는 경매상품 등록자로, 가격은 startPrice로 설정
            this.topBid = new Bid();
            this.topBid.setUser(bidProduct.getUser());
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
}

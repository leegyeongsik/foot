package com.foot.dto;

import com.foot.entity.BidProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BidProductResponseDto {

    //UserResponseDto

    private Long id;
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

    private List<BidResponseDto> bidResponseDtoList = new ArrayList<>();

    public BidProductResponseDto(BidProduct bidProduct) {
        id = bidProduct.getId();
        name = bidProduct.getName();
        description = bidProduct.getDescription();
        startPrice = bidProduct.getStartPrice();
        expirationPeriod = bidProduct.getExpirationPeriod();
        feetSize = bidProduct.getFeetsize();
        footSize = bidProduct.getFootsize();
        footPicture = bidProduct.getFootpicture();
        brand = new BrandResponseDto(bidProduct.getBrand());

        status = bidProduct.getStatus();

        createdAt = bidProduct.getCreatedAt();
        updatedAt = bidProduct.getUpdatedAt();

        for (int i = 0; i < bidProduct.getBids().size(); i++) {
            bidResponseDtoList.add(new BidResponseDto(bidProduct.getBids().get(i)));
        }
    }
}

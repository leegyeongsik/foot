package com.foot.dto.bidProduct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandBidProductCount {
    private String brand;
    private Long productCount;

    public BrandBidProductCount(String brand, Long productCount) {
        this.brand = brand;
        this.productCount = productCount;
    }
}
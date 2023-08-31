package com.foot.dto.products;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaleProductRequestDto {
//    String discountRate;
//    Long discountPrice;
    private List<Long> productIds;
    private double discountRate;
}

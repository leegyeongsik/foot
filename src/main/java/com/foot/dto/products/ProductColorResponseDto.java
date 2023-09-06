package com.foot.dto.products;

import com.foot.entity.ProductColor;
import com.foot.entity.ProductColorImg;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductColorResponseDto {
    Long colorId;
    Long colorAmount;
    String colorImg;
    List<ProductSizeResponseDto> productSizeResponseDtos;
    String colorName;
    public ProductColorResponseDto(ProductColorImg productColorImg , List<ProductSizeResponseDto> productSizeResponseDtos) {
        this.colorId = productColorImg.getId();
        this.colorImg=  productColorImg.getColorimg();
        this.productSizeResponseDtos = productSizeResponseDtos;
    }

    public ProductColorResponseDto(ProductColor productColor , ProductColorImg productColorImg) {
        this.colorId = productColor.getId();
        this.colorAmount=  productColor.getAmount();
        this.colorName = productColorImg.getColorname();
    }

}

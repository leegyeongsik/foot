package com.foot.dto.products;

import com.foot.dto.bidProduct.BrandResponseDto;
import lombok.Getter;

import java.util.List;

@Getter

public class ProductsResponseDto {
    List<ProductResponseDto> productResponseDtos;
    List<BrandResponseDto> brandResponseDtos;

    public ProductsResponseDto(List<ProductResponseDto> productResponseDtos , List<BrandResponseDto> brandResponseDtos){
        this.productResponseDtos = productResponseDtos;
        this.brandResponseDtos = brandResponseDtos;
    }


}

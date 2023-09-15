package com.foot.dto.products;

import lombok.Getter;

import java.util.List;

@Getter
public class innerProductResponseDto {
    ProductResponseDto productResponseDto;
    List<ProductColorResponseDto> productColors; // 하나의 컬러에 여러 사이즈가 존재하고 해당 사이즈 컬러의 재고 개수
    List<ProductSizeResponseDto> productSizeResponseDtos; // 업데이트할때 사이즈에 존재하는 컬러의 리스트
    List<ProductSizesResponseDto> productSizesResponseDtos;

    public innerProductResponseDto(ProductResponseDto product,
                                   List<ProductColorResponseDto> productColorResponseDtos , List<ProductSizesResponseDto> productSizesResponseDtos){
        this.productResponseDto = product;
        this.productColors = productColorResponseDtos;
        this.productSizesResponseDtos = productSizesResponseDtos;
    }

    public innerProductResponseDto(ProductResponseDto product,
                                   List<ProductSizeResponseDto> productSizeResponseDtos ){
        this.productResponseDto = product;
        this.productSizeResponseDtos = productSizeResponseDtos;
    }
}

package com.foot.dto.products;

import com.foot.entity.ProductSize;
import lombok.Getter;

@Getter
public class ProductSizesResponseDto {
    Long size;
    Long footSize;
    Long feetSize;

    public ProductSizesResponseDto(ProductSize productSize){
        this.size = productSize.getSize();;
        this.footSize = productSize.getFootsize();
        this.feetSize = productSize.getFeetsize();
    }
}

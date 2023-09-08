package com.foot.dto.products;

import com.foot.entity.ProductColor;
import com.foot.entity.ProductSize;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductSizeResponseDto {
    Long productSizeId;
    Long size;
    Long amount;
    Long footSize;
    Long feetSize;
    String footPicture;
    Long ColorAmount;
    List<ProductColorResponseDto> colorResponseDtos;
    Long ColorIds;
//    Long predictFootPicture;




    public ProductSizeResponseDto(ProductSize productSize , ProductColor productColor) {
        this.productSizeId = productSize.getId();
        this.size = productSize.getSize();
        this.amount = productSize.getAmount();
        this.footSize = productSize.getFootsize();
        this.feetSize = productSize.getFeetsize();
        this.footPicture = productSize.getFootpicture();
//        this.predictFootPicture = productFoot.getPredictfootpicture();
        this.ColorAmount = productColor.getAmount();
        this.ColorIds = productColor.getId();

    }

    public ProductSizeResponseDto(ProductSize productSize , List<ProductColorResponseDto> colorResponseDtos ) {
        this.productSizeId = productSize.getId();
        this.size = productSize.getSize();
        this.amount = productSize.getAmount();
        this.footSize = productSize.getFootsize();
        this.feetSize = productSize.getFeetsize();
        this.colorResponseDtos = colorResponseDtos;

    }
}

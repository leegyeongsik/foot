package com.foot.dto;

import com.foot.entity.Product;
import lombok.Getter;

@Getter
public class FavoriteResponseDto {
    Long productId;
    String modelName;
    String modelDescription;
    Long price;
    String ModelPicture;
    double discountRate;
    Long discountPrice;
    boolean isAppend;

    public FavoriteResponseDto(Product product , boolean IsAppend){
        this.productId = product.getId();
        this.modelName = product.getModel();
        this.modelDescription = product.getDescription();
        this.price = product.getPrice();
        this.ModelPicture = product.getModelpicture();
        this.discountRate = product.getDiscountRate();
        this.discountPrice = product.getDiscountPrice();
        this.isAppend = IsAppend;
    }
}

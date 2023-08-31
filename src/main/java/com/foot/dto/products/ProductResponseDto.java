package com.foot.dto.products;

import com.foot.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponseDto {
    Long productId;
    String modelName;
    String modelDescription;
    Long totalAmount;
    Long price;
    String ModelPicture;
    double discountRate;
    double discountPrice;
//    String brand;

    public ProductResponseDto(Product product){
        this.productId = product.getId();
        this.modelName = product.getModel();
        this.totalAmount = product.getTotalAmount();
        this.price = product.getPrice();
        this.ModelPicture= product.getModelpicture();
        this.discountRate = product.getDiscountRate();
        this.discountPrice = product.getDiscountPrice();
        this.modelDescription = product.getDescription();
//        this.brand =
    }
}

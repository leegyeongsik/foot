package com.foot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDto {
    private Long cartItemId; //장바구니 상품 아이디 -> 컬러 아이디


    private Long productId; // 상품 아이디

    private Long productSize;

    private String productName; //상품명  조인

    private Long price; //상품 금액 조인

    private int count; //수량 조인

    private String imgUrl; //상품 이미지 경로

    private Long productColorId;


    public CartResponseDto(Long cartItemId, Long productId, String productName, Long productSize, Long price, int count, String imgUrl, Long id) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.productSize = productSize;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
        this.productColorId = id;
    }
}

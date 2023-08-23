package com.foot.controller;

import com.foot.dto.BasketRequestDto;
import com.foot.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class BasketController {

    private final BasketService basketService;

    //장바구니에 상품 추가
    @PostMapping("/{productId}/basket")
    public void addBasket(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody BasketRequestDto requestDto) {
        basketService.addBasket(productId, userDetails.getUser(), requestDto);
    }

    //장바구니 상품 삭제
    @DeleteMapping("/{productId}/basket")
    public void deleteProductToBasket(@PathVariable Long productId) {

    }
}

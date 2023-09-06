package com.foot.controller;

import com.foot.dto.ApiResponseDto;
import com.foot.dto.BasketRequestDto;
import com.foot.entity.User;
import com.foot.security.UserDetailsImpl;
import com.foot.service.BasketService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    //장바구니 조회
    @GetMapping("/basket")
    public ResponseEntity<ApiResponseDto> getBasket(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return basketService.getBasket(userDetails.getUser());
    }

    //장바구니 특정 상품 삭제
    @DeleteMapping("/{productId}/basket")
    public void deleteProductToBasket(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        basketService.deleteProductToBasket(productId, userDetails.getUser());
    }

    //장바구니 전체 삭제
    @DeleteMapping("/basket")
    public void deleteBasket(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        basketService.deleteBasket(userDetails.getUser());
    }
}

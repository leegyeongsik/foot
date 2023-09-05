package com.foot.controller;

import com.foot.dto.CartItemDto;
import com.foot.dto.CartResponseDto;
import com.foot.security.UserDetailsImpl;
import com.foot.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니에 상품 추가
    @PostMapping("/api/cart")
    public void addCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CartItemDto cartItemDto) {
        cartService.addCart(userDetails.getUser(), cartItemDto);
    }

    // 장바구니 조회
    @GetMapping("/view/cart")
    public String getCartList(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<CartResponseDto> cartResponseDtoList = cartService.getCartList(userDetails.getUser());
        model.addAttribute("cartItems", cartResponseDtoList);
        return "cart";
    }

    // 장바구니 아이템 수량 변경
    @PatchMapping("/api/cart")
    public ResponseEntity<String> updateCartAmount(@RequestBody CartItemDto cartItemDto) {
        cartService.updateCartItemCount(cartItemDto.getItemId(), cartItemDto.getCount());
        return ResponseEntity.ok("CartItem updated successfully");
    }
}

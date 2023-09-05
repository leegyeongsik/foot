package com.foot.controller;

import com.foot.dto.CartItemDto;
import com.foot.dto.CartResponseDto;
import com.foot.security.UserDetailsImpl;
import com.foot.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니에 상품 추가
    @PostMapping("/api/product/cart")
    public void addCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CartItemDto cartItemDto) {
        cartService.addCart(userDetails.getUser(), cartItemDto);
    }

    @GetMapping("/view/cart")
    public String getCartList(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<CartResponseDto> cartResponseDtoList = cartService.getCartList(userDetails.getUser());
        model.addAttribute("cartItems", cartResponseDtoList);
        return "cart";
    }
}

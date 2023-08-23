package com.foot.controller;

import com.foot.dto.ApiResponseDto;
import com.foot.entity.Favorite;
import com.foot.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class FavoriteController {

    private final FavoriteService favoriteService;

    //상품 찜하기
    @PostMapping("/{productId}/favorite")
    public void likeProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        favoriteService.likeProduct(productId, userDetails.getUser());
    }

    //상품 찜하기 취소
    @DeleteMapping("/{productId}/favorite")
    public void unlikeProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        favoriteService.unlikeProduct(productId, userDetails.getUser());
    }
}

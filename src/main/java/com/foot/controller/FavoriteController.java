package com.foot.controller;

import com.foot.dto.ApiResponseDto;
import com.foot.dto.FavoriteRequestDto;
import com.foot.dto.FavoriteResponseDto;
import com.foot.entity.Favorite;
import com.foot.security.UserDetailsImpl;
import com.foot.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping({"/{productId}/favorite"})
    public void appendFavoriteProduct(@PathVariable Long productId , @AuthenticationPrincipal UserDetailsImpl userDetails ){
        favoriteService.appendFavoriteProduct(productId,userDetails.getUser());
    }

    @GetMapping({"/favorite"})
    public List<FavoriteResponseDto> getFavoriteProducts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return favoriteService.getFavoriteProducts(userDetails.getUser());
    }

    @DeleteMapping({"/{productId}/favorite"})
    public void deleteFavoriteProduct(@AuthenticationPrincipal UserDetailsImpl userDetails , @PathVariable Long productId){
        favoriteService.deleteFavoriteProduct(userDetails.getUser() , productId);
    }
}

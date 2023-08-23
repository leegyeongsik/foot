package com.foot.service;

import com.foot.entity.Favorite;
import com.foot.entity.Product;
import com.foot.entity.User;
import com.foot.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    //상품 찜하기
    public void likeProduct(Long productId, User user) {
        Product product = productRepository.findById(productId).orElseThrow();

        favoriteRepository.save(new Favorite(user, product));
    }

    //찜하기 취소
    public void unlikeProduct(Long productId, User user) {
        Product product = productRepository.findById(productId).orElseThrow();

        Favorite favorite = favoriteRepository.findByProductIdAndUserId(productId, user.getId()).orElseThrow();

        favoriteRepository.delete(favorite);
    }
}

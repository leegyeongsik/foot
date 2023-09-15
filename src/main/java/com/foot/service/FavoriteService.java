package com.foot.service;

import com.foot.dto.FavoriteRequestDto;
import com.foot.dto.FavoriteResponseDto;
import com.foot.entity.Favorite;
import com.foot.entity.Product;
import com.foot.entity.User;
import com.foot.repository.favorite.FavoriteRepository;
import com.foot.repository.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;


    public void appendFavoriteProduct(Long ProductId, User user) {
        Product product=productRepository.findById(ProductId).get();
        Favorite favorite = Favorite.builder()
                .product(product)
                .user(user)
                .build();
        favoriteRepository.save(favorite);
    }

    public void deleteFavoriteProduct(User user, Long productId) {
        Favorite favorite = favoriteRepository.getFavorite(user.getId(),productId);
        favoriteRepository.delete(favorite);
    }

    public List<FavoriteResponseDto> getFavoriteProducts(User user) {
        List<FavoriteResponseDto> favoriteResponseDtos = new ArrayList<>();

        List<Product> favorites=favoriteRepository.getFavorites(user.getId());
        for (Product favorite : favorites) {
            favoriteResponseDtos.add(new FavoriteResponseDto(favorite , true));
        }
        return favoriteResponseDtos;
    }
}

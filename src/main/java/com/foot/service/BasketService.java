package com.foot.service;

import com.foot.dto.ApiResponseDto;
import com.foot.dto.BasketRequestDto;
import com.foot.entity.Basket;
import com.foot.entity.ProductSize;
import com.foot.entity.User;
import com.foot.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductSizeRepository productSizeRepository;

    //장바구니에 추가
    public void addBasket(Long productId, User user, BasketRequestDto requestDto) {
        Long productSizeId = requestDto.getProductSizeId();

        ProductSize productSize = productSizeRepository.findById(productSizeId).orElseThrow();

        Basket basket = new Basket(user, productSize);

        basketRepository.save(basket);
    }

    //장바구니 조회
    public ResponseEntity<ApiResponseDto> getBasket(User user) {
        List<Basket> basketList = basketRepository.findByUserId(user.getId());

        return ResponseEntity.ok().body(new ApiResponseDto("장바구니 조회 완료", HttpStatus.OK.value(), basketList));
    }

    //장바구니 특정 상품 삭제
    public void deleteProductToBasket(Long productId, User user) {
        List<Basket> basketList = basketRepository.findByUserId(user.getId());

        for (Basket basket : basketList) {
            if (basket.getProductSize().getProduct().getId().equals(productId)) {
                basketRepository.delete(basket);
                break;
            }
        }
    }

    //장바구니 전체 삭제
    public void deleteBasket(User user) {
        List<Basket> basketList = basketRepository.findByUserId(user.getId());

        basketRepository.deleteAll(basketList);
    }
}

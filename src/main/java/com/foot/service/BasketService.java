package com.foot.service;

import com.foot.dto.BasketRequestDto;
import com.foot.entity.Basket;
import com.foot.entity.ProductSize;
import com.foot.entity.User;
import com.foot.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}

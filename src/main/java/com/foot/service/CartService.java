package com.foot.service;

import com.foot.dto.CartItemDto;
import com.foot.entity.Cart;
import com.foot.entity.CartItem;
import com.foot.entity.ProductSize;
import com.foot.entity.User;
import com.foot.repository.CartItemRepository;
import com.foot.repository.CartRepository;
import com.foot.repository.products.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductSizeRepository productSizeRepository;

    // 장바구니 상품 추가
    @Transactional
    public Long addCart(User user, CartItemDto cartItemDto) {
        ProductSize productSize = productSizeRepository.findById(cartItemDto.getItemId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
        );

        // 장바구니가 없는 유저면 장바구니 생성
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndProductSizeId(cart.getId(), productSize.getId());

        log.info("CartItem already exists with id: {}", savedCartItem.getId());
        if (savedCartItem != null) {
            // 장바구니에 이미 존재하는 상품이면 개수만 변경
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            // 장바구니에 없는 상품이면 담기
            CartItem cartItem = CartItem.createCartItem(cart, productSize, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }

    }
}

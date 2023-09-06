package com.foot.service;

import com.foot.dto.CartItemDto;
import com.foot.dto.CartResponseDto;
import com.foot.entity.*;
import com.foot.repository.CartItemRepository;
import com.foot.repository.CartRepository;
import com.foot.repository.products.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        int itemCountToAdd = cartItemDto.getCount();

        // 장바구니가 없는 유저면 장바구니 생성
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndProductSizeId(cart.getId(), productSize.getId());


        if (savedCartItem != null) {
            // 장바구니에 이미 존재하는 상품이면 개수만 추가
            // productSize의 amount 이상으로 못담게 제한
            int remainingSpace = (int) (productSize.getAmount() - savedCartItem.getCount());
            if (remainingSpace < itemCountToAdd) {
                itemCountToAdd = remainingSpace;
            }
            savedCartItem.addCount(itemCountToAdd);
            return savedCartItem.getId();
        } else {
            // 장바구니에 없는 상품이면 담기
            if (itemCountToAdd > productSize.getAmount()) {
                itemCountToAdd = Math.toIntExact(productSize.getAmount());
            }
            CartItem cartItem = CartItem.createCartItem(cart, productSize, itemCountToAdd);
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }


    // 장바구니 조회
    @Transactional(readOnly = true)
    public List<CartResponseDto> getCartList(User user) {
        List<CartResponseDto> cartResponseDtoList = new ArrayList<>();

        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            return cartResponseDtoList;
        }

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        cartResponseDtoList = cartItems.stream()
                .map(this::mapToCartResponseDto)
                .collect(Collectors.toList());

        return cartResponseDtoList;
    }

    // 장바구니 아이템 수량 변경
    @Transactional
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
                );

        ProductSize productSize = productSizeRepository.findById(cartItem.getProductSize().getId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
        );

        // count를 ProductSize의 amount로 제한
        if (count > productSize.getAmount()) {
            count = Math.toIntExact(productSize.getAmount());
        }
        cartItem.updateCount(count);
    }


    // 장바구니 아이템 삭제
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
                );
        cartItemRepository.delete(cartItem);
    }


    // CartItem 엔티티를 CartResponseDto로 매핑하는 메서드
    private CartResponseDto mapToCartResponseDto(CartItem cartItem) {
        ProductSize productSize = cartItem.getProductSize();
        Product product = productSize.getProduct();

        // 할인중일 경우 할인된 가격을 매핑하고 할인중이 아니면 그냥 price를 매핑한다.
        Long price = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();

        return new CartResponseDto(
                cartItem.getId(),
                product.getId(),
                product.getModel(),
                productSize.getSize(),
                price,
                cartItem.getCount(),
                product.getModelpicture()
        );
    }


}

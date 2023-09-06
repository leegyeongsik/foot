package com.foot.repository;

import com.foot.entity.Cart;
import com.foot.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndProductSizeId(Long cartId, Long productSizeId);

    List<CartItem> findByCart(Cart cart);
}

package com.foot.repository;

import com.foot.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndProductSizeId(Long cartId, Long productSizeId);
}

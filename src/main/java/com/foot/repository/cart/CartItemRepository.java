package com.foot.repository.cart;

import com.foot.entity.Cart;
import com.foot.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> ,CartItemRepositoryCustom{

    List<CartItem> findByCart(Cart cart);

    CartItem findByCartIdAndProductColorId(Long id, Long productColorId);
}

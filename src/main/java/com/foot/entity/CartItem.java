package com.foot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSizeColor_id")
    private ProductColor productColor;

    private int count;

    public static CartItem createCartItem(Cart cart, ProductColor productColor, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductColor(productColor);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }


}

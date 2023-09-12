package com.foot.repository.cart;

import com.foot.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor

public class CartItemRepositoryImpl implements CartItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QProductColor productColor = QProductColor.productColor;
    private final QProductSize productSize = QProductSize.productSize;
    private final QCartItem cartItem = QCartItem.cartItem;


    @Override
    public Map<ProductSize, ProductColor> getModelSizeOfColor(Long productColorId , Long CartItemId) {
            List<Tuple> tuples = jpaQueryFactory.select(productSize, productColor)
                    .from(cartItem)
                    .leftJoin(cartItem.productColor)
                    .leftJoin(productColor.productSize)
                    .where(
                            productColor.id.eq(productColorId).and(cartItem.id.eq(CartItemId))
                    )
                    .fetch();
            return tuples.stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            tuple -> tuple.get(productSize),
                            tuple -> tuple.get(productColor)
                    ));
        }


}

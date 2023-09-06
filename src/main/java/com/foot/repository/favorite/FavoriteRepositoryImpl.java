package com.foot.repository.favorite;


import com.foot.entity.*;
import com.foot.repository.products.ProductRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QFavorite favorite =QFavorite.favorite;
    private final QProduct product=QProduct.product;

    @Override
    public List<Product> getFavorites(Long UserId) {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, favorite.createdAt);

        return  jpaQueryFactory.select(product)
                .from(favorite)
                .leftJoin(favorite.user)
                .leftJoin(favorite.product)
                .where(
                        favorite.user.id.eq(UserId)
                )
                .orderBy(orderSpecifier)
                .fetch();
    }

    @Override
    public Favorite getFavorite(Long UserId, Long ProductId) {
        return  jpaQueryFactory.select(favorite)
                .from(favorite)
                .leftJoin(favorite.user)
                .leftJoin(favorite.product)
                .where(
                        favorite.user.id.eq(UserId).and(favorite.product.id.eq(ProductId))
                )
                .fetchFirst();
    }

    @Override
    public Optional<Favorite> getFavoriteIsExist(Long UserId, Long ProductId) {
        return Optional.ofNullable(jpaQueryFactory.select(favorite)
                .from(favorite)
                .leftJoin(favorite.user)
                .leftJoin(favorite.product)
                .where(
                        favorite.user.id.eq(UserId).and(favorite.product.id.eq(ProductId))
                )
                .fetchFirst());
    }
}



package com.foot.repository.products;


import com.foot.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QProductColorImg productColorImg =QProductColorImg.productColorImg;

    QProductColor productColor=QProductColor.productColor;

    QProduct product = QProduct.product;

    QProductSize productSize = QProductSize.productSize;

    QBrand brand = QBrand.brand;
    @Override
    public ProductColorImg getModelColor(String ModelColorName, Long ModelId) {
        return  jpaQueryFactory.select(productColorImg)
                .from(productColorImg)
                .leftJoin(productColorImg.product)
                .where(
                        getColor(ModelColorName,ModelId)
                )
                .fetchOne();
    }

    @Override
    public List<ProductSize> getModelSize(Long productId) {
        return jpaQueryFactory.select(productSize)
                .from(productSize)
                .leftJoin(productSize.product)
                .where(
                        getProductId(productId)
                )
                .fetch();
    }

    @Override
    public List<ProductColorImg> getModelColors(Long productId) {
        return jpaQueryFactory.select(productColorImg)
                .from(productColorImg)
                .leftJoin(productColorImg.product)
                .where(
                        getProductId(productId)
                )
                .fetch();
    }

    @Override
    public Map<ProductSize, ProductColor> getModelSizeOfColor(Long productId, Long id) {
        List<Tuple> tuples = jpaQueryFactory.select(productSize , productColor)
                .from(productColor)
                .leftJoin(productColor.productColorImg)
                .leftJoin(productColor.productSize)
                .where(
                        getProductIds(productId , id)
                )
                .fetch();
        return tuples.stream()
                .distinct()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(productSize),
                        tuple -> tuple.get(productColor)
                ));
    }

    @Override
    public List<Product> searchProduct(String name) {
        return jpaQueryFactory.selectFrom(product)
                .where(product.model.contains(name))
                .fetch();
    }

    @Override
    public Map<ProductColor, ProductColorImg> getSizeColorProduct(Long productSizeId, Long id) {
        List<Tuple> tuples = jpaQueryFactory.select(productColor , productColorImg)
                .from(productColor)
                .leftJoin(productColor.productColorImg)
                .leftJoin(productColor.productSize)
                .where(
                        getProductSizeColor(productSizeId ,id )
                )
                .fetch();
        return tuples.stream()
                .distinct()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(productColor),
                        tuple -> tuple.get(productColorImg)
                ));
    }

    @Override
    public Map<ProductSize, ProductColor> getOrderProduct(Long ProductcolorId) {
        List<Tuple> tuples = jpaQueryFactory.select(productSize , productColor)
                .from(productColor)
                .leftJoin(productColor.productSize)
                .where(
                        productColor.id.eq(ProductcolorId)
                )
                .fetch();
        return tuples.stream()
                .distinct()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(productSize),
                        tuple -> tuple.get(productColor)
                ));
    }

    @Override
    public List<Product> getBrandProduct(String brand) {
        return  jpaQueryFactory.select(product)
                .from(product)
                .leftJoin(product.brand)
                .where(
                        product.brand.name.eq(brand)
                )
                .fetch();

    }

    @Override
    public List<ProductSize> getSizes(Long productId) {
        return  jpaQueryFactory.select(productSize)
                .from(productSize)
                .leftJoin(productSize.product)
                .where(
                        productSize.product.id.eq(productId)
                )
                .fetch();
    }


    private BooleanExpression getColor(String ModelColorName,Long ModelId){
        return Objects.nonNull(ModelId) ? product.id.eq(ModelId)
                .and(productColorImg.colorname.eq(ModelColorName)): null;

    }
    private BooleanExpression getProductId(Long productId){
        return Objects.nonNull(productId) ? product.id.eq(productId) : null;

    }

    private BooleanExpression getProductIds(Long productId , Long id){
        return Objects.nonNull(productId) ? productColorImg.product.id.eq(productId).and(productColorImg.id.eq(id)) : null;

    }

    private BooleanExpression getProductSizeColor(Long productSizeId , Long id){
        return Objects.nonNull(productSizeId) ? productSize.product.id.eq(productSizeId).and(productSize.id.eq(id)) : null;

    }
}

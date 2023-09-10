package com.foot.repository.products;

import com.foot.entity.Product;
import com.foot.entity.ProductColor;
import com.foot.entity.ProductColorImg;
import com.foot.entity.ProductSize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository

public interface ProductRepositoryCustom {
    ProductColorImg getModelColor(String ModelColorName , Long ModelId);

    List<ProductSize> getModelSize(Long productId);
    List<ProductColorImg> getModelColors(Long productId);


    Map<ProductSize, ProductColor> getModelSizeOfColor(Long productId, Long id);
     List<Product> searchProduct(String name);

    Map<ProductColor, ProductColorImg> getSizeColorProduct(Long productSizeId, Long id);

    Map<ProductSize, ProductColor> getOrderProduct(Long ProductcolorId);

    List<Product> getBrandProduct(String brand);

    List<ProductSize> getSizes(Long productId);
}

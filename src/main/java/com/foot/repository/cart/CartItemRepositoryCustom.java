package com.foot.repository.cart;

import com.foot.entity.ChatLog;
import com.foot.entity.ProductColor;
import com.foot.entity.ProductSize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CartItemRepositoryCustom {
    Map<ProductSize, ProductColor> getModelSizeOfColor(Long productColorId,Long CartItemId);


}

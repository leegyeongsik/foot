package com.foot.repository.products;

import com.foot.entity.ProductColorImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductColorImgRepository extends JpaRepository<ProductColorImg,Long> {
}

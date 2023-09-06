package com.foot.repository.products;

import com.foot.entity.Product;
import com.foot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Page<Product> findByModelContaining(String searchKeyword, Pageable pageable);
}

package com.foot.repository;

import com.foot.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByProductIdAndUserId(Long productId, Long id);
}

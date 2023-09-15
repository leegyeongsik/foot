package com.foot.repository.favorite;

import com.foot.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository

public interface FavoriteRepositoryCustom {
    List<Product> getFavorites(Long UserId);
    Favorite getFavorite(Long UserId , Long ProductId);

    Optional<Favorite> getFavoriteIsExist(Long UserId , Long ProductId);
}

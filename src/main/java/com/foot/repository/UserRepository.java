package com.foot.repository;

import com.foot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
    Boolean existsByName(String username);

    Optional<User> findByEmailContaining(String keyword);

    Optional<User> findByEmail(String email);

    Page<User> findByNameContaining(String searchKeyword, Pageable pageable);


}

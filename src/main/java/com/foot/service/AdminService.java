package com.foot.service;

import com.foot.dto.AdminUserRequestDto;
import com.foot.dto.ProfileResponseDto;
import com.foot.dto.UserListResponseDto;
import com.foot.dto.products.SaleProductRequestDto;
import com.foot.entity.Product;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.UserRepository;
import com.foot.repository.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;

    // 전체 회원 목록 조회
    public Page<User> getUserList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // 회원 검색
    public Page<User> userSearchList(String keyword, Pageable pageable) {
        return userRepository.findByNameContaining(keyword, pageable);
    }

    // 회원 상세 조회
    public User getUser(Long id) {
        User user = findUser(id);
        return user;

    }


    // 회원 정보 수정
    public void updateUser(AdminUserRequestDto requestDto) {
        User user = findUser(requestDto.getUserId());

        // 비밀번호 변경창에 아무것도 입력하지 않았을 경우 변경하지 않고 유지
        if (!requestDto.getNewPassword().isEmpty()) {
            String newPassword = passwordEncoder.encode(requestDto.getNewPassword());
            user.setPassword(newPassword);
        }
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setAddress(requestDto.getAddress());
        user.setCellphone(requestDto.getCellphone());
        user.setRole(requestDto.getRole());

        userRepository.save(user);

    }

    // 회원 강제 탈퇴
    public void deleteUser(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }


    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("유저가 존재하지 않습니다.")
        );
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("상품이 존재하지 않습니다.")
                );
    }

    // 상품 전체 목록 조회
    public Page<Product> getProductList(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


    // 상품 검색
    public Page<Product> productSearchList(String searchKeyword, Pageable pageable) {
        return productRepository.findByModelContaining(searchKeyword, pageable);
    }

    // 상품 할인율 변경
    public void updateProductDiscountRates(SaleProductRequestDto requestDto) {
        List<Long> productIds = requestDto.getProductIds();
        double discountRate = requestDto.getDiscountRate();

        for (Long productId : productIds) {
            Product product = getProductById(productId);
            if (product != null) {
                // 할인율 업데이트
                product.setDiscountRate(discountRate);

                // 할인된 가격 계산 및 업데이트
                double discountedPrice = product.getPrice() * (1 - discountRate / 100);
                Long price = Math.round(discountedPrice);
                product.setDiscountPrice(price);

                // 상품 저장
                productRepository.save(product);
            }
        }
    }
}

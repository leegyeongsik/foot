package com.foot.service;

import com.foot.dto.AdminUserRequestDto;
import com.foot.dto.ProfileResponseDto;
import com.foot.dto.UserListResponseDto;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;

    // 전체 회원 목록 조회
    public Page<User> getUserList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // 회원 상세 조회
    public User getUser(Long id) {
        User user = findUser(id);
        return user;

    }

    // 회원 검색
    public Page<User> userSearchList(String keyword, Pageable pageable) {
        return userRepository.findByNameContaining(keyword, pageable);
    }

    // 회원 정보 수정
    @Transactional
    public void updateUser(AdminUserRequestDto requestDto) {
        User user = findUser(requestDto.getUserId());
        String newPassword = passwordEncoder.encode(requestDto.getNewPassword());

        user.setName(requestDto.getName());
        user.setPassword(newPassword);
        user.setEmail(requestDto.getEmail());
        user.setAddress(requestDto.getAddress());
        user.setCellphone(requestDto.getCellphone());
        if (requestDto.getRole() == "USER") {
            user.setRole(UserRoleEnum.USER);
        } else if (requestDto.getRole() == "ADMIN") {
            user.setRole(UserRoleEnum.ADMIN);
        }
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


}

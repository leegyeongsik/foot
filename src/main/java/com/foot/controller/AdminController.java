package com.foot.controller;

import com.foot.dto.AdminUserRequestDto;
import com.foot.dto.ProfileRequestDto;
import com.foot.dto.ProfileResponseDto;
import com.foot.dto.UserListResponseDto;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.UserRepository;
import com.foot.security.UserDetailsImpl;
import com.foot.service.AdminService;
import com.foot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;
    private final UserRepository userRepository;

    // 회원 정보 수정
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/profile")
    public ResponseEntity<String> updateUser(@RequestBody AdminUserRequestDto requestDto) {
        adminService.updateUser(requestDto);
        return ResponseEntity.ok().body("변경 성공");
    }


    // 회원 탈퇴

}

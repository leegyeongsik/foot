package com.foot.controller;

import com.foot.dto.ProfileResponseDto;
import com.foot.dto.UserListResponseDto;
import com.foot.entity.UserRoleEnum;
import com.foot.service.AdminService;
import com.foot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    // 전체 회원 목록 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/users")
    public ResponseEntity<UserListResponseDto> getUserList() {
        UserListResponseDto userList = adminService.getUserList();
        return ResponseEntity.ok().body(userList);
    }

    // 회원 상세 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/users/{id}")
    public ProfileResponseDto getUser(@PathVariable Long id) {
        return adminService.getUser(id);
    }



    // 회원 탈퇴



}

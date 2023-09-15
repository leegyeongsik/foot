package com.foot.controller;

import com.foot.dto.SignupRequestDto;
import com.foot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup") // 회원가입
    public String signup(SignupRequestDto requestDto) {
        userService.userSignup(requestDto);
        return "redirect:/view/login-page";
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

}

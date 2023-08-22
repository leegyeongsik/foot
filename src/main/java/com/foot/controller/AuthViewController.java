package com.foot.controller;

import com.foot.dto.ProfileResponseDto;
import com.foot.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/view")
@RequiredArgsConstructor
public class AuthViewController {

    // 로그인 페이지
    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }


    // 본인 프로필 조회
    @GetMapping("/profile")
    public String getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        ProfileResponseDto profile = new ProfileResponseDto(userDetails.getUser());
        model.addAttribute("profile", profile);
        return "profile";
    }



}

package com.foot.controller;

import com.foot.dto.ProfileResponseDto;
import com.foot.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public String loginPage(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies(); // 모든 쿠키의 정보를 cookies에 저장
        if(cookies != null){ // 쿠키가 한개라도 있으면 실행
            for(int i=0; i< cookies.length; i++){
                cookies[i].setMaxAge(0); // 유효시간을 0으로 설정
                response.addCookie(cookies[i]); // 응답 헤더에 추가
            }
        }

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

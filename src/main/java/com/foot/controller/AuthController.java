package com.foot.controller;

import com.foot.dto.SignupRequestDto;
import com.foot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup") // 회원가입
    public void signup(@RequestBody SignupRequestDto requestDto) {
        userService.userSignup(requestDto);
    }

}

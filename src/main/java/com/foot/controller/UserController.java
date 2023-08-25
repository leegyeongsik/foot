package com.foot.controller;

import com.foot.dto.PasswordRequestDto;
import com.foot.dto.ProfileRequestDto;
import com.foot.dto.ProfileResponseDto;
import com.foot.dto.SignupRequestDto;
import com.foot.security.UserDetailsImpl;
import com.foot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 비밀번호 확인
    @PostMapping("/profile/password")
    public ResponseEntity<String> checkPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto passwordRequestDto) {
        return userService.confirmPassword(userDetails, passwordRequestDto);
    }


    // 회원 정보 수정
    @PutMapping("/profile")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto) {
        ProfileResponseDto profile = userService.updateUser(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body("변경 성공");
    }


    // 회원 탈퇴
    @DeleteMapping("/profile")
    public void deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser());
    }

    @PutMapping("/{userId}/foot")
    public void signup( @RequestBody SignupRequestDto requestDto , @PathVariable Long userId) {
        userService.updateUserFoot(requestDto);
    }

}

package com.foot.controller;

import com.foot.dto.*;
import com.foot.security.UserDetailsImpl;
import com.foot.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    // 회원 정보 수정
    @PutMapping("/profile")
    public ResponseEntity<ProfileResponseDto> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto) {
        ProfileResponseDto profile = userService.updateUser(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(profile);
    }


    // 탈퇴
    @DeleteMapping("/profile")
    public void deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        userService.deleteUser(userDetails.getUser(), requestDto);
    }

    @PutMapping("/{userId}/foot")
    public void signup( @RequestBody SignupRequestDto requestDto , @PathVariable Long userId) {
        userService.updateUserFoot(requestDto);
    }

}

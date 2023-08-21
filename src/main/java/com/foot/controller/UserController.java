package com.foot.controller;

import com.foot.dto.LoginRequestDto;
import com.foot.dto.SignupRequestDto;
import com.foot.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    UserService userService;


//    @PostMapping("/login") // auth 로 빼기
//    public void login(@RequestBody LoginRequestDto requestDto) {
//        userService.Userlogin(requestDto);
//    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody SignupRequestDto requestDto , @PathVariable Long userId) {
        userService.updateUser(requestDto , userId);
    }

    @DeleteMapping("/{userId}")
    public void signup(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}/foot")
    public void signup( @RequestBody SignupRequestDto requestDto , @PathVariable Long userId) {
        userService.updateUserFoot(requestDto);
    }
}

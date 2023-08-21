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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponseDto> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto) {
        ProfileResponseDto profile = userService.updateUser(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(profile);
    }

    @DeleteMapping("/profile")
    public void deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        userService.deleteUser(userDetails.getUser(), requestDto);
    }

    @PutMapping("/{userId}/foot")
    public void signup( @RequestBody SignupRequestDto requestDto , @PathVariable Long userId) {
        userService.updateUserFoot(requestDto);
    }
}

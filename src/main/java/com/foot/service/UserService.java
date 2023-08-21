package com.foot.service;

import com.foot.dto.LoginRequestDto;
import com.foot.dto.SignupRequestDto;
import com.foot.entity.User;
import com.foot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void userSignup(SignupRequestDto requestDto) {
        if(userRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(password)
                .cellphone(requestDto.getCellphone())
                .address(requestDto.getAddress())
                .role(requestDto.getRole())
                .userimage(requestDto.getUserimage())
                .build();

        userRepository.save(user);

    }

    public void updateUser(SignupRequestDto requestDto, Long userId) {
    }

    public void deleteUser(Long requestDto) {
    }

    public void updateUserFoot(SignupRequestDto requestDto) {
    }


}

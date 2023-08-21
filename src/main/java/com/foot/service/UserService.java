package com.foot.service;

import com.foot.dto.*;
import com.foot.entity.User;
import com.foot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
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
                .userImage(requestDto.getUserImage())
                .build();

        userRepository.save(user);

    }



    public void updateUserFoot(SignupRequestDto requestDto) {
    }


    // 회원 정보 수정
    @Transactional
    public ProfileResponseDto updateUser(User user, ProfileRequestDto requestDto) {

        User currentUser = userRepository.findByName(user.getName()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        String newPassword = passwordEncoder.encode(requestDto.getNewPassword());

        currentUser.setName(requestDto.getName());
        currentUser.setPassword(newPassword);
        currentUser.setEmail(requestDto.getEmail());
        currentUser.setAddress(requestDto.getAddress());
        currentUser.setCellphone(requestDto.getCellphone());
        currentUser.setUserImage(requestDto.getUserImage());
        return new ProfileResponseDto(currentUser);
    }


    // 회원 탈퇴
    public void deleteUser(User user, PasswordRequestDto requestDto) {

        User currentUser = userRepository.findByName(user.getName()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        userRepository.delete(currentUser);
    }
}

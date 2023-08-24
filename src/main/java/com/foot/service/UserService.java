package com.foot.service;

import com.foot.dto.*;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.UserRepository;
import com.foot.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원 가입
    public void userSignup(SignupRequestDto requestDto) {
        if(userRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }


        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }


        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(password)
                .cellphone(requestDto.getCellphone())
                .address(requestDto.getAddress())
                .role(role)
                .userImage("")
                .build();

        userRepository.save(user);

    }

    // 비밀번호 확인
    public ResponseEntity<String> confirmPassword(UserDetailsImpl userDetails, PasswordRequestDto passwordRequestDto) {
        log.info(userDetails.getPassword());
        log.info(passwordRequestDto.getPassword());

        if (passwordEncoder.matches(passwordRequestDto.getPassword(), userDetails.getPassword())) {
            log.info("성공");
            return ResponseEntity.ok("Success");
        } else {
            log.info("실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error"); // 상태 코드 400 반환
        }
    }

    // 회원 정보 수정
    @Transactional
    public ProfileResponseDto updateUser(User user, ProfileRequestDto requestDto) {

        User currentUser = userRepository.findByName(user.getName()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );


        String newPassword = passwordEncoder.encode(requestDto.getNewPassword());

        currentUser.setName(requestDto.getName());
        currentUser.setPassword(newPassword);
        currentUser.setEmail(requestDto.getEmail());
        currentUser.setAddress(requestDto.getAddress());
        currentUser.setCellphone(requestDto.getCellphone());
        //currentUser.setUserImage(requestDto.getUserImage());
        userRepository.save(currentUser);
        return new ProfileResponseDto(currentUser);
    }


    // 회원 탈퇴
    public void deleteUser(User user) {

        User currentUser = userRepository.findByName(user.getName()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        userRepository.delete(currentUser);
    }

    public void updateUserFoot(SignupRequestDto requestDto) {
    }
}

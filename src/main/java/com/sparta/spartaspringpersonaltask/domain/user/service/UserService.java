package com.sparta.spartaspringpersonaltask.domain.user.service;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.dto.user.SignupRequestDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.DuplicateException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원 가입 기능
     * @param requestDto 별명, 유저이름, 비밀번호, 이메일, 관리자여부(선택), 관리자토큰코드(선택)
     * @param role 권한(기본 : USER)
     * @return 회원가입 성공 메세지
     */
    @Transactional
    public String signup(SignupRequestDto requestDto, UserRoleEnum role) {
        User user = toEntity(requestDto, role);

        Optional<User> checkUsername = userRepository.findByUserName(user.getUserName());
        if (checkUsername.isPresent()) {
            throw new DuplicateException("중복된 사용자가 존재합니다.");
        }

        Optional<User> checkEmail = userRepository.findByEmail(user.getEmail());
        if (checkEmail.isPresent()) {
            throw new DuplicateException("중복된 Email 입니다.");
        }

        userRepository.save(user);

        return role + "권한으로 회원가입에 성공 하였습니다.";
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );
    }

    private User toEntity(SignupRequestDto requestDto, UserRoleEnum role) {
        return User.builder()
                .userNickname(requestDto.getUserNickname())
                .userName(requestDto.getUserName())
                .userPassword(passwordEncoder.encode(requestDto.getUserPassword()))
                .email(requestDto.getEmail())
                .role(role)
                .build();
    }
}

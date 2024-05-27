package com.sparta.spartaspringpersonaltask.domain.user.controller;

import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.domain.user.service.UserService;
import com.sparta.spartaspringpersonaltask.global.dto.user.SignupRequestDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Value("${admin.token}")
    private String ADMIN_TOKEN;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 가입 기능
     * @param requestDto 별명, 유저이름, 비밀번호, 이메일, 관리자여부(선택), 관리자토큰코드(선택)
     * @return 회원 가입 성공 메세지
     */
    @PostMapping("/user/signup")
    public String signup(@Valid @RequestBody SignupRequestDto requestDto) {
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new InvalidException("관리자 암호가 일치하지 않습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        return userService.signup(requestDto, role);
    }
}

package com.sparta.spartaspringpersonaltask.global.auth;

import com.sparta.spartaspringpersonaltask.global.dto.auth.LoginRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.auth.SignupRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 회원 가입 기능
     *
     * @param requestDto 회원가입 정보 객체
     * @return 회원가입 성공 메세지
     */
    @PostMapping("/sign-up")
    public String signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return authService.signup(requestDto);
    }

    /**
     * 로그인 기능
     *
     * @param requestDto 로그인 정보 객체
     * @param response 토큰을 담아줄 Http 객체
     * @return 억세스 토큰, 리플레시 토큰을 헤더에 담고 로그인성공메세지 반환
     */
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        authService.login(requestDto, response);
        return "로그인에 성공하였습니다.";
    }

    /**
     * API 요청중 토큰만료로 401 에러가 뜬경우 프론트에서 요청
     *
     * @param request 리플레시 토큰을 헤더에 담은 Http 객체
     * @param response 새로운 억세스토큰과 리플레시 토큰을 담아 반환해줄 Http 객체
     */
    @PostMapping("/token/refresh")
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        authService.refreshAccessToken(request, response);
    }
}

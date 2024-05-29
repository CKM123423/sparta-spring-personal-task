package com.sparta.spartaspringpersonaltask.global.auth;

import com.sparta.spartaspringpersonaltask.global.auth.jwt.JwtAuthResponse;
import com.sparta.spartaspringpersonaltask.global.dto.auth.LoginRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.auth.SignupRequestDto;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/sign-up")
    public String signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return authService.signup(requestDto);
    }

    @PostMapping("/login")
    public JwtAuthResponse login(@Valid @RequestBody LoginRequestDto requestDto) {
        return authService.login(requestDto);
    }

    @PostMapping("/token/refresh")
    public JwtAuthResponse refreshAccessToken(HttpServletRequest request) {
        return authService.refreshAccessToken(request);
    }
}

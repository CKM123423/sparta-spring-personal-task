package com.sparta.spartaspringpersonaltask.global.auth.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
// 토큰 테스트용으로 작성해놨었음
public class JwtTokenTestDto {
    private String accessToken;
    private String refreshToken;
}

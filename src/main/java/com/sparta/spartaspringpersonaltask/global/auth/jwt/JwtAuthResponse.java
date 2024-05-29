package com.sparta.spartaspringpersonaltask.global.auth.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtAuthResponse {
    private String accessToken;
    private String refreshToken;
}

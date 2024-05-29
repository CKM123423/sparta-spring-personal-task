package com.sparta.spartaspringpersonaltask.global.security;

import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String accessToken = jwtProvider.getJwtFromHeader(request, JwtProvider.AUTHORIZATION_HEADER);
        String refreshToken = jwtProvider.getJwtFromHeader(request, JwtProvider.REFRESH_HEADER);

        if (StringUtils.hasText(accessToken) || StringUtils.hasText(refreshToken)) { // token 존재 확인
            if (jwtProvider.validateToken(accessToken, JwtProvider.AUTHORIZATION_HEADER)) { // access token 유효성 확인
                Claims claims = jwtProvider.getUserInfoFromToken(accessToken);
                try {
                    setAuthentication(claims.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            } else {
                if (jwtProvider.validateToken(refreshToken, JwtProvider.REFRESH_HEADER)) {
                    Claims refreshClaims = jwtProvider.getUserInfoFromToken(refreshToken);
                    String username = refreshClaims.getSubject();

                    // 유저 정보를 통해 권한 확인
                    UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
                    UserRoleEnum role = userDetails.getUser().getRole();

                    // 토큰 재 발행
                    String newAccessToken = jwtProvider.createAccessToken(username, role);
                    String newRefreshToken = jwtProvider.createRefreshToken(username);

                    // 토큰 헤더에 추가
                    response.addHeader(JwtProvider.AUTHORIZATION_HEADER, newAccessToken);
                    response.addHeader(JwtProvider.REFRESH_HEADER, newRefreshToken);

                    try {
                        setAuthentication(username);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return;
                    }
                } else {
                    log.error("리프레시 토큰이 유효하지 않거나 거부되었습니다.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "다시 로그인 해주세요");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
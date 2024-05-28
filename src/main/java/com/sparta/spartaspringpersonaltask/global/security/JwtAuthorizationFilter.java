package com.sparta.spartaspringpersonaltask.global.security;

import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.global.utils.jwt.JwtUtil;
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

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getJwtFromHeader(request, JwtUtil.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(accessToken)) { // access token 내용확인
            if (jwtUtil.validateToken(accessToken, JwtUtil.AUTHORIZATION_HEADER)) { // access token 유효성 확인

                Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
                setAuthentication(claims.getSubject());

            } else {
                String refreshToken = jwtUtil.getJwtFromHeader(request, JwtUtil.REFRESH_HEADER);

                if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken, JwtUtil.REFRESH_HEADER)) {
                    Claims refreshClaims = jwtUtil.getUserInfoFromToken(refreshToken);
                    String username = refreshClaims.getSubject();

                    // 유저 정보를 통해 권한 확인
                    UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
                    UserRoleEnum role = userDetails.getUser().getRole();

                    String newAccessToken = jwtUtil.createToken(username, role);
                    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
                    setAuthentication(username);
                } else {
                    log.error("리프레시 토큰이 유효하지 않거나 거부되었습니다.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "다시 로그인 해주세요");
                    return;
                }
            }
        } else {
            log.error("억세스 토큰이 존재하지 않거나 거부되었습니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "다시 로그인 해주세요");
            return;
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
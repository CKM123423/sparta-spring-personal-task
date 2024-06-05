package com.sparta.spartaspringpersonaltask.global.auth;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.auth.jwt.JwtProvider;
import com.sparta.spartaspringpersonaltask.global.dto.auth.LoginRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.auth.SignupRequestDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.DuplicateException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${admin.code}")
    private String ADMIN_CODE;

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    /**
     * 회원 가입 기능
     *
     * @param requestDto 가입을 요청한 회원정보
     * @return 회원 가입 성공 메세지
     */
    @Transactional
    public String signup(SignupRequestDto requestDto) {
        // 권한 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_CODE.equals(requestDto.getAdminCode())) {
                throw new InvalidException("관리자 암호가 일치하지 않습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        // 객체 생성
        String encodedPassword = passwordEncoder.encode(requestDto.getUserPassword());
        User user = requestDto.toEntity(encodedPassword, role);

        Optional<User> checkUsername = userRepository.findByUsername(user.getUsername());
        if (checkUsername.isPresent()) {
            throw new DuplicateException("중복된 사용자가 존재합니다.");
        }

        Optional<User> checkEmail = userRepository.findByUserEmail(user.getUserEmail());
        if (checkEmail.isPresent()) {
            throw new DuplicateException("중복된 Email 입니다.");
        }

        userRepository.save(user);

        return "회원가입에 성공 하였습니다.";

    }

    /**
     * 로그인 기능
     *
     * @param requestDto 아이디, 비밀번호
     * @param response HTTP 반환 객체
     */
    public void login(LoginRequestDto requestDto, HttpServletResponse response) {
        // 유저 조회
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );

        // 유저 유효성 확인후 저장
        user.validatePassword(requestDto.getPassword(), passwordEncoder);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 주입
        addJwtTokens(user.getUsername(), authentication.getAuthorities(), response);
    }

    /**
     * 새로운 엑세스 토큰을 발급받기 위해 리프레시 토큰을 검증
     *
     * @param request  Http 요청 객체
     * @param response Http 반환 객체
     */
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(JwtProvider.REFRESH_HEADER);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(JwtProvider.BEARER_PREFIX)) {
            refreshToken = refreshToken.substring(7);
        }
        jwtProvider.validateToken(refreshToken);
        String username = jwtProvider.getUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        addJwtTokens(username, userDetails.getAuthorities(), response);
    }

    /**
     * 토큰 발행 로직
     *
     * @param username 유저 ID
     * @param authorities 유저 권한 데이터를 가지고 있는 객체
     * @param response 헤더에 토큰을 넣어주기 위한 Http 객체
     */
    private void addJwtTokens(String username,
                              Collection<? extends GrantedAuthority> authorities,
                              HttpServletResponse response) {
        String accessToken = jwtProvider.createAccessToken(username, authorities);
        String refreshToken = jwtProvider.createRefreshToken(username);

        response.addHeader(JwtProvider.ACCESS_HEADER, accessToken);
        response.addHeader(JwtProvider.REFRESH_HEADER, refreshToken);

    }
}

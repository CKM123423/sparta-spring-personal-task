package com.sparta.spartaspringpersonaltask.global.auth;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.auth.jwt.JwtAuthResponse;
import com.sparta.spartaspringpersonaltask.global.auth.jwt.JwtProvider;
import com.sparta.spartaspringpersonaltask.global.dto.auth.LoginRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.auth.SignupRequestDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.DuplicateException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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

    @Value("${admin.token}")
    private String ADMIN_TOKEN;

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
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new InvalidException("관리자 암호가 일치하지 않습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        // 객체 생성
        String encodedPassword = passwordEncoder.encode(requestDto.getUserPassword());
        User user = requestDto.toEntity(encodedPassword, role);

        Optional<User> checkUsername = userRepository.findByUserName(user.getUserName());
        if (checkUsername.isPresent()) {
            throw new DuplicateException("중복된 사용자가 존재합니다.");
        }

        Optional<User> checkEmail = userRepository.findByEmail(user.getEmail());
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
     * @return 토큰 정보
     */
    public JwtAuthResponse login(LoginRequestDto requestDto) {
        // 유저 조회
        User user = userRepository.findByUserName(requestDto.getUsername()).orElseThrow(
                () -> new NotFoundException("유저를 찾을 수 없습니다.")
        );

        // 유저 유효성 확인후 저장
        user.validatePassword(requestDto.getPassword(), passwordEncoder);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
                        requestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 발행
        return createJwtToken(user.getUserName(), authentication.getAuthorities());
    }

    /**
     * 새로운 엑세스 토큰을 발급받기 위해 리프레시 토큰을 검증
     *
     * @param request HTTP 요청 객체
     * @return 새로 발급된 토큰 정보
     */
    public JwtAuthResponse refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(JwtProvider.REFRESH_HEADER);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(JwtProvider.BEARER_PREFIX)) {
            refreshToken = refreshToken.substring(7);
        }

        String username = jwtProvider.getUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return createJwtToken(username, userDetails.getAuthorities());
    }

    private JwtAuthResponse createJwtToken(String username, Collection<? extends GrantedAuthority> authorities) {
        String accessToken = jwtProvider.createAccessToken(username, authorities);
        String refreshToken = jwtProvider.createRefreshToken(username);

        return new JwtAuthResponse(accessToken, refreshToken);
    }
}

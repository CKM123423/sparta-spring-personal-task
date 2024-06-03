package com.sparta.spartaspringpersonaltask.global.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * AuthenticationEntryPoint 는 Spring Security 에서 예외가 발생시 반환되는 익셉션을 감지하여 후처리를 수행
 *
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("handlerExceptionResolver") // 여러개의 빈이 등록되어있는데 ()안의 빈을 특정해서 빈으로 등록하는 어노테이션
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        resolver.resolveException(request, response, null, authException);
    }
}
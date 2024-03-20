package com.board.aop;

import com.board.exception.UnauthorizedException;
import com.board.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private static final String TOKEN_TYPE = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Pointcut("execution(* com.board.api.*.*Controller.*(..))")
    public void beforeApi() {
    }

    @Pointcut("execution(* com.board.api.auth.*Controller.*(..))")
    public void authApi() {
    }

    @Pointcut("@annotation(com.board.annotation.NoAuth)")
    public void noAuthAnnotation() {

    }

    @Before("beforeApi() && !authApi() && !noAuthAnnotation()")
    public void authentication() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 액세스 토큰 추출
        String accessToken = Optional.ofNullable(request.getHeader("Authorization"))
                .map(token -> token.substring(TOKEN_TYPE.length()))
                .orElseThrow(() -> new UnauthorizedException("인증 정보가 존재하지 않습니다."));

        jwtTokenProvider.getAuthentication(accessToken);

        // TODO:: 회원 엔티티 설계 후 추가 구현
    }

}

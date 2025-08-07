package com.board.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Deprecated(since = "2024-04-23")
public class AuthenticationAspect {

    @Pointcut("execution(* com.board.api.*.*Controller.*(..))")
    public void beforeApi() {
    }

    @Pointcut("execution(* com.board.api.auth.*Controller.*(..))")
    public void authApi() {
    }

    @Pointcut("@annotation(com.board.annotation.NoAuth)")
    public void noAuthAnnotation() {
    }

    // @Before("beforeApi() && !authApi() && !noAuthAnnotation()")
    public void authentication() {
    }

}

package com.board.support.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityUserFactory.class)
public @interface WithCustomSecurityUser {

    long memberId() default 1L;

    String username() default "khghouse@naver.com";

    String role() default "USER";

}

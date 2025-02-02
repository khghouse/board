package com.board.support.security;

import com.board.dto.security.SecurityUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class SecurityUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType()
                .isAssignableFrom(SecurityUser.class);

        // 파라미터가 @AuthenticationPrincipal을 사용한 경우에만 처리
        // return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // 실제로 주입할 객체를 반환
        return new SecurityUser(1L, "khghouse@naver.com", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

}

package com.board.configuration;

import com.board.component.Redis;
import com.board.filter.JwtAuthenticationFilter;
import com.board.filter.JwtExceptionHandlerFilter;
import com.board.handler.SecurityAccessDeniedHandler;
import com.board.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Redis redis;
    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error")
                .requestMatchers(toH2Console());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable) // REST API 환경에서 JWT 토큰으로 인증 -> 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // Bearer 토큰 인증 방식 사용 -> 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // json 데이터를 통한 로그인 -> 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음
                .authorizeHttpRequests((authorize) -> authorize // 요청에 대한 인가 규칙 정의
                        .requestMatchers("/api/v1/auth/**").permitAll() // 해당 API는 인증, 인가 없이 접근 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(new SecurityAccessDeniedHandler(objectMapper)))
                .userDetailsService(userDetailsService)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redis), UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter 실행 전에 JwtAuthenticationFilter를 실행
                .addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class) // JwtAuthenticationFilter에서 발생한 예외를 처리하기 위해 JwtExceptionHandlerFilter를 먼저 실행한다.
                .build();
    }

}

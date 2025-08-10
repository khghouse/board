package com.board.provider;

import com.board.global.security.JwtTokenProvider;
import com.board.support.IntegrationTestSupport;
import com.board.domain.member.entity.Member;
import com.board.domain.member.repository.MemberRepository;
import com.board.global.security.JwtToken;
import com.board.global.security.SecurityUser;
import com.board.global.security.JwtErrorCode;
import com.board.global.common.exception.ForbiddenException;
import com.board.global.security.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class JwtTokenProviderTest extends IntegrationTestSupport {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("토큰을 생성하고 액세스, 리프레쉬 토큰을 확인한다.")
    void generateToken() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();
        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("khghouse@daum.net", null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);

        // when
        JwtToken result = jwtTokenProvider.generateToken(authenticate);

        // then
        assertThat(result).isNotNull();
        jwtTokenProvider.validateAccessToken(result.getAccessToken());
        jwtTokenProvider.validateRefreshToken(result.getRefreshToken());
    }


    @Test
    @DisplayName("액세스 토큰으로 인증 객체를 리턴하고 확인한다.")
    void getAuthentication() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();
        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("khghouse@daum.net", null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticate);

        // when
        Authentication result = jwtTokenProvider.getAuthentication(jwtToken.getAccessToken());
        SecurityUser user = (SecurityUser) result.getPrincipal();

        // then
        assertThat(result.getName()).isEqualTo("khghouse@daum.net");
        assertThat(user.getEmail()).isEqualTo("khghouse@daum.net");
        assertThat(user.getMemberId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("액세스 토큰으로 인증 객체를 리턴하지만 권한 정보가 없어서 예외가 발생한다.")
    void getAuthenticationNotAuthorized() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();
        memberRepository.save(member);

        String accessToken = Jwts.builder()
                .subject("khghouse@daum.net")
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("2z6oNf/GwepEYumNk5rJSIyADL+WQ3YrArbVv+LTQJs=")))
                .claim("memberId", 1L)
                .claim("email", "khghouse@daum.net")
                .expiration(new Date(System.currentTimeMillis() + 3600L * 1000L))
                .compact();

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.getAuthentication(accessToken))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("권한 정보가 없는 토큰입니다.");
    }

    @Test
    @DisplayName("액세스 토큰을 이용하여 회원 ID를 조회한다.")
    void getMemberIdByAccessToken() {
        // given
        String accessToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("2z6oNf/GwepEYumNk5rJSIyADL+WQ3YrArbVv+LTQJs=")))
                .claim("memberId", 1L)
                .expiration(new Date(System.currentTimeMillis() + 3600L * 1000L))
                .compact();

        // when
        Long result = jwtTokenProvider.getMemberIdByAccessToken(accessToken);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("만료된 액세스 토큰을 이용하여 회원 ID를 조회한다.")
    void getMemberIdByExpiredAccessToken() throws Exception {
        // given
        String accessToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("2z6oNf/GwepEYumNk5rJSIyADL+WQ3YrArbVv+LTQJs=")))
                .claim("memberId", 1L)
                .expiration(new Date(System.currentTimeMillis() + 1000L))
                .compact();

        TimeUnit.SECONDS.sleep(1);

        // when
        Long result = jwtTokenProvider.getMemberIdByAccessToken(accessToken);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("액세스 토큰의 유효성을 검증하고 성공한다.")
    void validateToken() {
        // given
        String accessToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("2z6oNf/GwepEYumNk5rJSIyADL+WQ3YrArbVv+LTQJs=")))
                .expiration(new Date(System.currentTimeMillis() + 3600L * 1000L))
                .compact();

        // when
        boolean result = jwtTokenProvider.validateAccessToken(accessToken);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("액세스 토큰의 유효성 체크에서 손상된 토큰으로 예외가 발생한다.")
    void validateTokenMalformedJwtException() {
        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.validateAccessToken("json.web.token"))
                .isInstanceOf(JwtException.class)
                .satisfies(e -> {
                    JwtException jwtException = (JwtException) e;
                    assertThat(jwtException.getJwtErrorCode()).isEqualTo(JwtErrorCode.MALFORMED);
                });
    }

    @Test
    @DisplayName("액세스 토큰의 유효성 체크에서 만료된 토큰으로 예외가 발생한다.")
    void validateTokenExpiredJwtException() throws Exception {
        // given
        String accessToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("2z6oNf/GwepEYumNk5rJSIyADL+WQ3YrArbVv+LTQJs=")))
                .expiration(new Date(System.currentTimeMillis() + 1000L))
                .compact();

        TimeUnit.SECONDS.sleep(1);

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.validateAccessToken(accessToken))
                .isInstanceOf(JwtException.class)
                .satisfies(e -> {
                    JwtException jwtException = (JwtException) e;
                    assertThat(jwtException.getJwtErrorCode()).isEqualTo(JwtErrorCode.EXPIRED);
                });
    }

    @Test
    @DisplayName("다른 키 값으로 생성된 액세스 토큰은 유효성 체크에서 예외가 발생한다.")
    void validateTokenInvalidKey() {
        // given
        String accessToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("yXbYyWTRsYQwoe6Tm+z5ujQ/Irmi34d3QEzJQAxuRwQ=")))
                .expiration(new Date(System.currentTimeMillis() + 3600L * 1000L))
                .compact();

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.validateAccessToken(accessToken))
                .isInstanceOf(JwtException.class)
                .satisfies(e -> {
                    JwtException jwtException = (JwtException) e;
                    assertThat(jwtException.getJwtErrorCode()).isEqualTo(JwtErrorCode.INVALID);
                });
    }

}
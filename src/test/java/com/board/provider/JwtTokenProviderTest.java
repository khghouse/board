package com.board.provider;

import com.board.IntegrationTestSupport;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.jwt.JwtToken;
import com.board.dto.security.SecurityUser;
import com.board.exception.ForbiddenException;
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
        jwtTokenProvider.validateToken(result.getAccessToken());
        jwtTokenProvider.validateTokenByRefreshToken(result.getRefreshToken());
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
        assertThat(user.getMemberId()).isEqualTo(1L);
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
//
//    @Test
//    @DisplayName("생성된 액세스 토큰을 파싱했지만, 유효기간 만료로 예외가 발생한다.")
//    void getAuthenticationExpiration() throws Exception {
//        // given
//        ReflectionTestUtils.setField(jwtTokenProvider, "expirationMillisecord", 1L);
//        String accessToken = jwtTokenProvider.createAccessToken(1L);
//        TimeUnit.SECONDS.sleep(1);
//
//        // when, then
//        assertThatThrownBy(() -> jwtTokenProvider.getAuthentication(accessToken))
//                .isInstanceOf(UnauthorizedException.class)
//                .hasMessage("인증 정보가 유효하지 않습니다.");
//    }

}
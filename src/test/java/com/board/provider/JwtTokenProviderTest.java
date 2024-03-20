package com.board.provider;

import com.board.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "secret", "b1jU7YyAZqwCYI8KrMrJrUThs1yd3DkTc/FytboO/pk=");
        ReflectionTestUtils.setField(jwtTokenProvider, "expirationMillisecord", 3600L);
        jwtTokenProvider.init();
    }

    @Test
    @DisplayName("JWT key를 생성하고 Base64로 변환한다.")
    void createKey() {
        // given
        SecretKey key = Jwts.SIG.HS256.key()
                .build();

        // when
        String result = Encoders.BASE64.encode(key.getEncoded());

        // then
        assertThat(result).isNotNull();
        assertThat(Base64.getDecoder()
                .decode(result)
                .length).isEqualTo(32);
        System.out.println("created key : " + result);
    }

    @Test
    @DisplayName("JWT 액세스 토큰을 생성한다.")
    void createAccessToken() {
        // when
        String result = jwtTokenProvider.createAccessToken();

        // then
        assertThat(result).isNotNull();
        System.out.println("created accessToken : " + result);
    }

    @Test
    @DisplayName("생성된 액세스 토큰을 파싱힌다.")
    void getAuthentication() throws Exception {
        // given
        String accessToken = jwtTokenProvider.createAccessToken();

        // when
        Claims result = jwtTokenProvider.getAuthentication(accessToken);

        // then
        assertThat(result.getSubject()).isEqualTo("Joe");
    }

    @Test
    @DisplayName("생성된 액세스 토큰을 파싱했지만, 유효기간 만료로 예외가 발생한다.")
    void getAuthenticationExpiration() throws Exception {
        // given
        ReflectionTestUtils.setField(jwtTokenProvider, "expirationMillisecord", 1L);
        String accessToken = jwtTokenProvider.createAccessToken();
        TimeUnit.SECONDS.sleep(1);

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.getAuthentication(accessToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("인증 정보가 유효하지 않습니다.");
    }

}
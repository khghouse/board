package com.board.global.infrastructure.redis;

import com.board.global.common.exception.BusinessException;
import com.board.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.board.global.common.enumeration.ErrorCode.INVALID_AUTHENTICATION;

@Component
@RequiredArgsConstructor
public class Redis {

    private final String PREFIX_REDIS_KEY_REFRESH_TOKEN = "refreshToken:";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue()
                .set(key, value, timeout, unit);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void setRefreshToken(Long memberId, String refreshToken) {
        redisTemplate.opsForValue()
                .set(PREFIX_REDIS_KEY_REFRESH_TOKEN + memberId, refreshToken, jwtTokenProvider.getRefreshTokenExpirationSeconds(), TimeUnit.SECONDS);
    }

    public void compareRefreshToken(Long memberId, String refreshToken) {
        // 해당 회원의 리프레쉬 토큰을 레디스에서 조회
        String redisRefreshToken = redisTemplate.opsForValue()
                .get(PREFIX_REDIS_KEY_REFRESH_TOKEN + memberId);

        // 클라이언트로 전달받은 리프레쉬 토큰과 비교
        if (!refreshToken.equals(redisRefreshToken)) {
            throw new BusinessException(INVALID_AUTHENTICATION);
        }
    }

    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(PREFIX_REDIS_KEY_REFRESH_TOKEN + memberId);
    }

    public void logoutAccessToken(String accessToken) {
        redisTemplate.opsForValue()
                .set(accessToken, "logout", jwtTokenProvider.getAccessTokenExpirationSeconds(), TimeUnit.SECONDS);
    }

}

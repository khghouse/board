package com.board.provider;

import com.board.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token.expiration-millisecord}")
    private Long expirationMillisecord;

    private SecretKey accessKey;

    @PostConstruct
    void init() {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createAccessToken() {
        return Jwts.builder()
                .subject("Joe")
                .signWith(accessKey)
                .expiration(getExpiration())
                .compact();
    }

    public Claims getAuthentication(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(accessKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다.");
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다.");
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + expirationMillisecord * 1000L);
    }

}

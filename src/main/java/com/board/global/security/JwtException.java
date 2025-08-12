package com.board.global.security;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class JwtException extends RuntimeException {

    private final JwtErrorCode jwtErrorCode;

    public JwtException(JwtErrorCode jwtErrorCode) {
        super(jwtErrorCode.getMessage());
        this.jwtErrorCode = jwtErrorCode;
    }

}
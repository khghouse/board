package com.board.global.security;

import com.board.global.security.JwtErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class JwtException extends RuntimeException {

    private JwtErrorCode jwtErrorCode;

    public JwtException(String message) {
        super(message);
    }

    public JwtException(JwtErrorCode jwtErrorCode) {
        super(jwtErrorCode.getMessage());
        this.jwtErrorCode = jwtErrorCode;
    }

}
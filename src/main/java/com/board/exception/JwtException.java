package com.board.exception;

import com.board.enumeration.JwtErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {

    private JwtErrorCode jwtErrorCode;

    public JwtException() {
        super();
    }

    public JwtException(String message) {
        super(message);
    }

    public JwtException(JwtErrorCode jwtErrorCode) {
        this.jwtErrorCode = jwtErrorCode;
    }

}
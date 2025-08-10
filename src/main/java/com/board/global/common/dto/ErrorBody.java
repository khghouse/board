package com.board.global.common.dto;

import com.board.global.common.enumeration.ErrorCode;
import com.board.global.security.JwtErrorCode;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
public class ErrorBody {

    private final String code;
    private final String message;

    private ErrorBody(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorBody fromErrorCode(ErrorCode errorCode) {
        return new ErrorBody(errorCode.name(), errorCode.getMessage());
    }

    public static ErrorBody badRequest(String message) {
        return new ErrorBody(BAD_REQUEST.name(), message);
    }

    public static ErrorBody unauthorized(String message) {
        return new ErrorBody(UNAUTHORIZED.name(), message);
    }

    public static ErrorBody jwtUnauthorized(JwtErrorCode jwtErrorCode) {
        return new ErrorBody(jwtErrorCode.name(), jwtErrorCode.getMessage());
    }

    public static ErrorBody forbidden(String message) {
        return new ErrorBody(FORBIDDEN.name(), message);
    }

    public static ErrorBody unprocessableEntity(String message) {
        return new ErrorBody(UNPROCESSABLE_ENTITY.name(), message);
    }

}
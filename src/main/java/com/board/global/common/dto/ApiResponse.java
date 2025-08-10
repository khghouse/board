package com.board.global.common.dto;

import com.board.global.common.enumeration.ErrorCode;
import com.board.global.security.JwtErrorCode;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
public class ApiResponse<T> {

    private final int status;
    private final boolean success;
    private T data;
    private ErrorBody error;

    private ApiResponse(int status, boolean success) {
        this.status = status;
        this.success = success;
    }

    private ApiResponse(int status, boolean success, T data) {
        this.status = status;
        this.success = success;
        this.data = data;
    }

    private ApiResponse(int status, boolean success, ErrorBody error) {
        this.status = status;
        this.success = success;
        this.error = error;
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(OK.value(), true);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(OK.value(), true, data);
    }

    public static ApiResponse<Void> badRequest(String errorMessage) {
        return new ApiResponse<>(BAD_REQUEST.value(), false, ErrorBody.badRequest(errorMessage));
    }

    public static ApiResponse<Void> unauthorized(String errorMessage) {
        return new ApiResponse<>(UNAUTHORIZED.value(), false, ErrorBody.unauthorized(errorMessage));
    }

    public static ApiResponse<Void> jwtUnauthorized(JwtErrorCode jwtErrorCode) {
        return new ApiResponse<>(UNAUTHORIZED.value(), false, ErrorBody.jwtUnauthorized(jwtErrorCode));
    }

    public static ApiResponse<Void> forbidden(String errorMessage) {
        return new ApiResponse<>(FORBIDDEN.value(), false, ErrorBody.forbidden(errorMessage));
    }

    public static ApiResponse<Void> notFound(ErrorCode errorCode) {
        return new ApiResponse<>(NOT_FOUND.value(), false, ErrorBody.fromErrorCode(errorCode));
    }

    public static ApiResponse<Void> unprocessableEntity(String errorMessage) {
        return new ApiResponse<>(UNPROCESSABLE_ENTITY.value(), false, ErrorBody.unprocessableEntity(errorMessage));
    }
}
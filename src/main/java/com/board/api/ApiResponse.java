package com.board.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse {

    public static final int SC_BUSINESS_EXCEPTION = 240;

    private int status;
    private Object data;
    private Object error;

    private ApiResponse(int status, Object data, Object error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse of(HttpStatus httpStatus, Object data, Object error) {
        return new ApiResponse(httpStatus.value(), data, error);
    }

    public static ApiResponse ok() {
        return new ApiResponse(HttpStatus.OK.value(), null, null);
    }

    public static ApiResponse ok(Object data) {
        return new ApiResponse(HttpStatus.OK.value(), data, null);
    }

    public static ApiResponse badRequest(Object error) {
        return new ApiResponse(HttpStatus.BAD_REQUEST.value(), null, error);
    }

    public static ApiResponse businessException(Object error) {
        return new ApiResponse(SC_BUSINESS_EXCEPTION, null, error);
    }

}

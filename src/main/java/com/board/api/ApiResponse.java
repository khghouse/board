package com.board.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse {

    private int status;
    private Object data;
    private Object error;

    private ApiResponse(HttpStatus httpStatus, Object data, Object error) {
        this.status = httpStatus.value();
        this.data = data;
        this.error = error;
    }

    public static ApiResponse of(HttpStatus httpStatus, Object data, Object error) {
        return new ApiResponse(httpStatus, data, error);
    }

    public static ApiResponse ok(Object data) {
        return new ApiResponse(HttpStatus.OK, data, null);
    }

    public static ApiResponse badRequest(Object error) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, null, error);
    }

}

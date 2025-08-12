package com.board.global.common.handler;

import com.board.global.common.dto.ApiResponse;
import com.board.global.common.exception.*;
import com.board.global.security.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse bindException(BindException e) {
        return ApiResponse.badRequest(e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
    }

    /**
     * PathVariable 변수의 값을 전달 받을 때, 데이터 타입이 일치하지 않는 경우 <br />
     * - /articles/10 -> o <br />
     * - /articles/abc -> x <br />
     * - /articles/1.2 -> x <br />
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse methodArgumentTypeMismatchException() {
        return ApiResponse.badRequest("요청 파라미터 타입이 올바르지 않습니다.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse<?> unauthorizedException(UnauthorizedException e) {
        return ApiResponse.unauthorized(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ApiResponse jwtException(JwtException e) {
        return ApiResponse.jwtUnauthorized(e.getJwtErrorCode());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse forbiddenException(ForbiddenException e) {
        return ApiResponse.forbidden(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<?> notFoundException(NotFoundException e) {
        return ApiResponse.notFound(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ApiResponse<?> conflictException(ConflictException e) {
        return ApiResponse.conflict(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnprocessableEntityException.class)
    public ApiResponse unprocessableEntityException(UnprocessableEntityException e) {
        return ApiResponse.unprocessableEntity(e.getErrorCode());
    }

}

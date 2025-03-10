package com.board.handler;

import com.board.api.ApiResponse;
import com.board.exception.BusinessException;
import com.board.exception.ForbiddenException;
import com.board.exception.JwtException;
import com.board.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;

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

    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse noSuchElementException(NoSuchElementException e) {
        return ApiResponse.businessException(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponse businessException(BusinessException e) {
        return ApiResponse.businessException(e.getMessage());
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
    public ApiResponse unauthorizedException(UnauthorizedException e) {
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ApiResponse jwtException(JwtException e) {
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null, e.getJwtErrorCode().getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse forbiddenException(ForbiddenException e) {
        return ApiResponse.of(HttpStatus.FORBIDDEN, null, e.getMessage());
    }

}

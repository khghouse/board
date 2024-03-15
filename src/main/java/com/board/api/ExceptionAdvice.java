package com.board.api;

import com.board.exceptions.BusinessException;
import com.board.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse bindException(BindException e) {
        return ApiResponse.badRequest(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse noSuchElementException(NoSuchElementException e) {
        return ApiResponse.businessException(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponse businessException(BusinessException e) {
        return ApiResponse.businessException(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse unauthorizedException(UnauthorizedException e) {
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null, e.getMessage());
    }

}

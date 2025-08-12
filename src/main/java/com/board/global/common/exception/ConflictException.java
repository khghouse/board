package com.board.global.common.exception;

import com.board.global.common.enumeration.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

    private final ErrorCode errorCode;

    public ConflictException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
}

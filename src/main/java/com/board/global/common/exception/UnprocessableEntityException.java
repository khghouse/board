package com.board.global.common.exception;

import com.board.global.common.enumeration.ErrorCode;
import lombok.Getter;

@Getter
public class UnprocessableEntityException extends RuntimeException {

    private ErrorCode errorCode;
    
    public UnprocessableEntityException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatMessage(args));
        this.errorCode = errorCode;
    }

    public UnprocessableEntityException(String message) {
        super(message);
    }

}

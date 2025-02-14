package com.board.exception;

import com.board.enumeration.ErrorCode;

public class BusinessException extends RuntimeException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public BusinessException(String message) {
        super(message);
    }

}

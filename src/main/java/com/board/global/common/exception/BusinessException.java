package com.board.global.common.exception;

import com.board.global.common.enumeration.ErrorCode;

public class BusinessException extends RuntimeException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public BusinessException(ErrorCode errorCode, int maxLength) {
        super(String.format("%s [최대 %d자]", errorCode.getMessage(), maxLength));
    }

    public BusinessException(String message) {
        super(message);
    }

}

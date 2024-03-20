package com.board.exception;

public class BusinessException extends RuntimeException {

    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

}

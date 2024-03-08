package com.board.exceptions;

public class BusinessException extends RuntimeException {

    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

}

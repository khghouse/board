package com.board.exceptions;

public class UnauthorizedException extends RuntimeException {

    private String message;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

}

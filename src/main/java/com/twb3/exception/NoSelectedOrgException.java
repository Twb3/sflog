package com.twb3.exception;

public class NoSelectedOrgException extends RuntimeException {

    private final String exceptionMessage;

    public NoSelectedOrgException(String message, String exceptionMessage) {
        super(message);
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
}

package com.twb3.exception;

public class OrgNotFoundException extends RuntimeException {

    private final String exceptionMessage;

    public OrgNotFoundException(String message, String exceptionMessage) {
        super(message);
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
}

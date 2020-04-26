package com.twb3.exception;

public class ConfigurationNotFoundException extends RuntimeException {

    private final String exceptionMessage;

    public ConfigurationNotFoundException(String message, String exceptionMessage) {
        super(message);
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
}

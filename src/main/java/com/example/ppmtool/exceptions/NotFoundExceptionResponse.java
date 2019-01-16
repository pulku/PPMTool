package com.example.ppmtool.exceptions;

public class NotFoundExceptionResponse {
    private String errorMessage;

    public NotFoundExceptionResponse(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

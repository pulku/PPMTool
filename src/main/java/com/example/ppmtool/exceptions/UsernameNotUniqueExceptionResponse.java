package com.example.ppmtool.exceptions;

public class UsernameNotUniqueExceptionResponse {

    private String username;

    public UsernameNotUniqueExceptionResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

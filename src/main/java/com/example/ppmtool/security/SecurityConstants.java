package com.example.ppmtool.security;

public class SecurityConstants {
    public static final String SIGN_UP_URL = "/api/users/**";
    public static final String H2_URL = "/h2-console/**";
    public static final String SECRET = "SecretKey";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADERS = "Authorization";
    public static final long EXPIRATION_TIME = 60_000;
}

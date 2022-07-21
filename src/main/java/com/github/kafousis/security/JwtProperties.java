package com.github.kafousis.security;

public class JwtProperties {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET = "SomeSecretForJWTGeneration";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 864_000_000; // 10 days
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 2_592_000_000L; // 30 days
    public static final String ACCESS_TOKEN_HEADER = "access_token";
    public static final String REFRESH_TOKEN_HEADER = "refresh_token";
}

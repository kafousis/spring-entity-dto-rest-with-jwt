package com.github.kafousis.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kafousis.security.JwtProperties;
import com.github.kafousis.security.LoginModel;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecurityUtils {

    private static final Algorithm algorithm = Algorithm.HMAC256(JwtProperties.SECRET.getBytes());

    public static String decodeJwtToken(String authHeader){
        String token = authHeader.substring(JwtProperties.TOKEN_PREFIX.length());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }
    public static String generateJwtToken(String username, String issuer, long duration){
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .withIssuer(issuer)
                .sign(algorithm);
        return  token;
    }

    public static LoginModel readCredentials(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        LoginModel loginModel = new LoginModel();
        loginModel.setUsername(username);
        loginModel.setPassword(password);
        return loginModel;
    }

    public static LoginModel readCredentials(ServletInputStream inputStream) throws IOException {
        return new ObjectMapper().readValue(inputStream, LoginModel.class);
    }

    public static Map<String, Object> buildErrorBody(int status, String error, String path){
        // LinkedHashMap items keeps inserted order
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("timestamp", AppUtils.getCurrentTimestamp());
        errorBody.put("status", status);
        errorBody.put("error", error);
        errorBody.put("path", path);
        return errorBody;
    }
}

package com.github.kafousis.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kafousis.utils.SecurityUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, Object> errorBody = SecurityUtils.buildErrorBody(UNAUTHORIZED.value(), "Unauthorized", request.getServletPath());
        new ObjectMapper().writeValue(response.getOutputStream(), errorBody);
    }
}

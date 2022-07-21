package com.github.kafousis.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kafousis.utils.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        log.info("Attempting Authentication");

        String username = null;
        String password = null;
        try {

            LoginModel loginModel = SecurityUtils.readCredentials(request.getInputStream());
            username = loginModel.getUsername();
            password = loginModel.getPassword();

            log.info("Username is: {}", username);
            log.info("Password is: {}", password);

            if (username == null || password == null){
                // empty or valid json not containing username and password
                throw new AccessDeniedException("Null username or password");
            }else{
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                return authenticationManager.authenticate(authenticationToken);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            Map<String, Object> errorBody = SecurityUtils.buildErrorBody(FORBIDDEN.value(), "Forbidden", request.getServletPath());
            new ObjectMapper().writeValue(response.getOutputStream(), errorBody);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {

        log.info("Authentication is successful");

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String accessToken = SecurityUtils.generateJwtToken(
                userDetails.getUsername(), request.getRequestURL().toString(),
                JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);

        String refreshToken = SecurityUtils.generateJwtToken(
                userDetails.getUsername(), request.getRequestURL().toString(),
                JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME);

        /* return tokens in json body
        Map<String, String> tokens = new HashMap<>();
        tokens.put(JwtProperties.ACCESS_TOKEN_HEADER, accessToken);
        tokens.put(JwtProperties.REFRESH_TOKEN_HEADER, refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        **/

        // return tokens in headers
        response.addHeader(JwtProperties.ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(JwtProperties.REFRESH_TOKEN_HEADER, refreshToken);
    }
}

package com.github.kafousis.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kafousis.entities.User;
import com.github.kafousis.repositories.UserRepository;
import com.github.kafousis.security.JwtProperties;
import com.github.kafousis.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @Slf4j
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(JwtProperties.TOKEN_PREFIX)){

            try {

                String refreshToken = authHeader.substring(JwtProperties.TOKEN_PREFIX.length());
                String username = SecurityUtils.decodeJwtToken(authHeader);

                Optional<User> userByUsername = userRepository.findByUsernameJoinFetch(username);

                if (userByUsername.isPresent()) {

                    String accessToken = SecurityUtils.generateJwtToken(
                            userByUsername.get().getUsername(), request.getRequestURL().toString(),
                            JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);

                    log.info("New access token is generated");

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

            }catch (Exception e){
                log.error("Error in token: " + e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                Map<String, Object> errorBody = SecurityUtils.buildErrorBody(FORBIDDEN.value(), "Forbidden", request.getServletPath());
                new ObjectMapper().writeValue(response.getOutputStream(), errorBody);
            }

        } else {
            log.error("Missing Authorization Header");
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            Map<String, Object> errorBody = SecurityUtils.buildErrorBody(FORBIDDEN.value(), "Forbidden", request.getServletPath());
            new ObjectMapper().writeValue(response.getOutputStream(), errorBody);
        }

    }
}

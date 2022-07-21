package com.github.kafousis.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kafousis.entities.User;
import com.github.kafousis.repositories.UserRepository;
import com.github.kafousis.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //log.info("Servlet path is {}", request.getServletPath());
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/token/refresh")){
            // if user tries to login continue the request
            filterChain.doFilter(request, response);
        } else {

            // if not, token has to be verified
            String authHeader = request.getHeader(AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith(JwtProperties.TOKEN_PREFIX)){

                try {
                    String username = SecurityUtils.decodeJwtToken(authHeader);
                    Optional<User> userByUsername = userRepository.findByUsernameJoinFetch(username);

                    if (userByUsername.isPresent()) {
                        UserDetailsImpl userDetails = new UserDetailsImpl(userByUsername.get());

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails.getUsername(),
                                        userDetails.getPassword(),
                                        userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    }

                }catch (Exception e){
                    log.error("Error in token: " + e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    Map<String, Object> errorBody = SecurityUtils.buildErrorBody(FORBIDDEN.value(), "Forbidden", request.getServletPath());
                    new ObjectMapper().writeValue(response.getOutputStream(), errorBody);
                }

            } else {
                log.error("No Authorization Header");
                filterChain.doFilter(request, response);
            }
        }
    }
}

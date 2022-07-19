package com.github.kafousis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/h2-console/**"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // no session will be created or used by Spring Security
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // Cross-site request forgery (CSRF) is a web security vulnerability
                // If our stateless API uses token-based authentication, such as JWT,
                // we don't need CSRF protection, and we must disable it
                .csrf().disable()

                // Spring Security disables rendering within an iframe because it can cause security issues
                // H2 console runs within a frame so while Spring security is enabled,
                // frame options has to be disabled explicitly, in order to get the H2 console working
                .headers().frameOptions().disable()

                .and()

                .authorizeRequests()
                    .antMatchers(AUTH_WHITELIST).permitAll();

        return http.build();
    }
}

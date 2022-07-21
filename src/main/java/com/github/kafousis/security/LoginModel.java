package com.github.kafousis.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginModel {
    private String username;
    private String password;
}

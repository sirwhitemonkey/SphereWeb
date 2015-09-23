package com.xmdevelopments.rest.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@SuppressWarnings("serial")
public class WsseAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public WsseAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
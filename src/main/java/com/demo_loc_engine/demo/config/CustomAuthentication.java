package com.demo_loc_engine.demo.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthentication implements AuthenticationProvider {

    private final UserDetailsLogin userDetailsLogin;

    public CustomAuthentication(UserDetailsLogin userDetailsLogin) {
        this.userDetailsLogin = userDetailsLogin;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println(authentication.getName());
        return new UsernamePasswordAuthenticationToken(userDetailsLogin.getUsername(),
                userDetailsLogin.getPassword(), userDetailsLogin.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supports'");
    }

    // public Authentication myAuthentication(Authentication authentication){

    // }

}

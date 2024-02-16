package com.demo_loc_engine.demo.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomAuthentication implements AuthenticationProvider {

    private final UserDetailsLogin userDetailsLogin;

    public CustomAuthentication(UserDetailsLogin userDetailsLogin) {
        this.userDetailsLogin = userDetailsLogin;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // System.out.println(authentication.getName());
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String userIp = details.getRemoteAddress();
        System.out.println("userIp :" + userIp);
        return new UsernamePasswordAuthenticationToken(userDetailsLogin.getUsername(),
                userDetailsLogin.getPassword(), userDetailsLogin.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    // public Authentication myAuthentication(Authentication authentication){

    // }

}

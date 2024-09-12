package com.demo_loc_engine.demo.config;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class IpFilter {

    private final Set<String> blockedIps = new HashSet<>();

    public IpFilter() {
        // Add IPs you want to block
        blockedIps.add("127.0.0.1");
        blockedIps.add("0:0:0:0:0:0:0:1");
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //     // System.out.println("your ip: "+request.getRemoteAddr());
    //     String clientIP = request.getRemoteAddr();
    //     if (blockedIps.contains(clientIP)) {
    //         response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    //         response.getWriter().write("Access Denied for this IP Address");
    //         return;
    //     }
    //     filterChain.doFilter(request, response);
    // }
}


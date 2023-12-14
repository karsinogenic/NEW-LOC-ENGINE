package com.demo_loc_engine.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        /**
         * Below is the custom security configurations
         */

        // http.authorizeHttpRequests()
        // .requestMatchers("/api/**")
        // .authenticated()
        // // .requestMatchers("/assets/**", "/media/**", "/js/**", "/image/**",
        // // "/plugins/**", "/sass/**", "/login1")
        // // .permitAll()
        // .and().formLogin()
        // // .and().formLogin().loginPage("/login1").loginProcessingUrl("/login")
        // .and().httpBasic();

        // http.authorizeHttpRequests()
        // .requestMatchers(HttpMethod.POST, "/api/score").authenticated()
        // .and()
        // .oauth2Client();

        http.csrf().disable()
                .authorizeHttpRequests()
                // .requestMatchers("/api/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/ceklogin").permitAll()
                .requestMatchers("/api/**").authenticated()
                .and().formLogin()
                .and().httpBasic();

        return http.build();

        // http.authorizeHttpRequests().anyRequest().denyAll()
        // .and().formLogin()
        // .and().httpBasic();
        // return http.build();

        // http.authorizeHttpRequests().anyRequest().permitAll()
        // .and().formLogin()
        // .and().httpBasic();
        // return http.build();

    }

    // @Bean
    // public UserDetailsService userDetailsService(DataSource datasource) {
    // return new JdbcUserDetailsManager(datasource);
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}

package com.lussomovano.backend.config;

import com.lussomovano.backend.security.JwtFilter;
import com.lussomovano.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/products/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

//package com.lussomovano.backend.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        logger.info("Initializing Security Filter Chain");
//
//        http
//                .csrf(csrf -> {
//                    csrf.disable();
//                    logger.debug("CSRF protection disabled");
//                })
//                .authorizeHttpRequests(auth -> {
//                    auth
//                            .requestMatchers("/api/auth/**", "/api/products/**").permitAll();
//                    logger.debug("Permitted endpoints: /api/auth/** and /api/products/**");
//
//                    auth
//                            .anyRequest().authenticated();
//                    logger.debug("All other endpoints require authentication");
//                })
//                .formLogin(form -> {
//                    form.disable();
//                    logger.debug("Form login disabled");
//                })
//                .httpBasic(httpBasic -> {
//                    logger.debug("HTTP Basic authentication enabled");
//                });
//
//        logger.info("Security Filter Chain initialized successfully");
//
//        return http.build();
//    }
//}

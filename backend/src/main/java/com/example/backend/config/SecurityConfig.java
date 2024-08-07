package com.example.backend.config;

import com.example.backend.jwt.JWTFilter;
import com.example.backend.jwt.JWTUtil;
import com.example.backend.jwt.LoginFilter;
import com.example.backend.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private CustomUserDetailsService userDetailsService;
    private JWTUtil jwtUtil;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JWTUtil jwtUtil) {

        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); //포트 허용
                        configuration.setAllowedMethods(Collections.singletonList("*")); //허용할 메서드
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*")); //허용할 헤더
                        configuration.setMaxAge(3600L); //허용 시간

                        configuration.setExposedHeaders(Collections.singletonList("Authorization")); //Authorization 헤더도 허용

                        return configuration;
                    }
                })));

        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        String loginUrl = "/api/account/login";
        http
                .addFilterAt(new LoginFilter(authenticationManager, jwtUtil, loginUrl), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}

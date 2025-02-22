package com.example.backend.config;

import com.example.backend.jwt.CustomLogoutFilter;
import com.example.backend.jwt.JWTFilter;
import com.example.backend.jwt.JWTUtil;
import com.example.backend.jwt.LoginFilter;
import com.example.backend.repository.RefreshRepository;
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
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private CustomUserDetailsService userDetailsService;
    private JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
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

        http.csrf(csrf -> csrf.disable());

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); //포트 허용
//                        configuration.setAllowedOrigins(Collections.singletonList("https://whattowear.store"));
                        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://whattowear.store"));
//                        configuration.setAllowedOriginPatterns(List.of("*"));
                        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                        configuration.setAllowedHeaders(List.of("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setMaxAge(3600L); //허용 시간
                        
//                        configuration.setExposedHeaders(Collections.singletonList("Authorization")); //Authorization 헤더도 허용

                        return configuration;
                    }
                })));

        http.formLogin((auth) -> auth.disable());

        http.httpBasic((auth) -> auth.disable());

        http.authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/fonts/**", "/static/**", "/", "/index.html", "/favicon.ico", "/js/**", "/css/**", "/img/**").permitAll()
                    .requestMatchers("/api/main/**").permitAll()
                    .requestMatchers("/api/account/**", "/api/outfit/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll()); // 2.17 추가

        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        String loginUrl = "/api/main/login";

        http.addFilterAt(new LoginFilter(authenticationManager, jwtUtil, loginUrl, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LoginFilter.class);

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}

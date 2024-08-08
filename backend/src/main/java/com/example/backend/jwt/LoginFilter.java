package com.example.backend.jwt;

import com.example.backend.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, String loginUrl) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl(loginUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);

            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            System.out.println("Email = " + email);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

            return authenticationManager.authenticate(authToken);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 클라이언트 요청에서 email, password 추출

        /*
        String email = obtainEmail(request);
        String password = obtainPassword(request);

        System.out.println("Eamil = " + email);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
        */

    }

    // LoginRequest 클래스를 내부 또는 외부 클래스로 정의
    public static class LoginRequest {
        private String email;
        private String password;

        // getters and setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /*
    protected String obtainEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }

    */

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = customUserDetails.getMember().getId();
        String username = customUserDetails.getUsername(); //사실 email


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        //일단 되긴 함~~~~ 성공
        //username 변수를 왜 그냥 두었냐면 jwtUtil 때문에.. 어떻게 처리할지 생각
        String token = jwtUtil.createJwt(id, username, role, 60*60*10*1000L);

//        Cookie cookie = new Cookie("Authorization", "Bearer " + token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Only Https
//        cookie.setPath("/");
//        cookie.setMaxAge(60*60*10);
//
//        response.addCookie(cookie);
        Cookie cookie = new Cookie("id", id.toString());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true); // Only Https
        cookie.setPath("/");
        cookie.setMaxAge(60*60*10);

        response.addCookie(cookie);

        //Authorization: Bearer 인증토큰string
        response.addHeader("Authorization", "Bearer " + token);

        System.out.println("success~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        System.out.println("fail~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        response.setStatus(401);
    }

}

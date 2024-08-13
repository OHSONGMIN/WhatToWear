package com.example.backend.jwt;

import com.example.backend.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

            return authenticationManager.authenticate(authToken);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

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


        //유저 정보 - username, email 해결해야함
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername(); //사실 email
        //String username = authentication.getName(); //편리하지만 데이터를 email로 저장했기 때문에 사용할 수 없을 듯...

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

//        -----------------AccessToken만 발급하는 코드----------------------
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        Integer id = customUserDetails.getMember().getId();
//        String username = customUserDetails.getUsername(); //사실 email
//
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();
//
//        //일단 되긴 함~~~~ 성공
//        //username 변수를 왜 그냥 두었냐면 jwtUtil 때문에.. 어떻게 처리할지 생각
//        String token = jwtUtil.createJwt(id, username, role, 60*60*10*1000L);
//
//        Cookie cookie = new Cookie("id", id.toString());
//        cookie.setHttpOnly(true);
//        //cookie.setSecure(true); // Only Https
//        cookie.setPath("/");
//        cookie.setMaxAge(60*60*10);
//
//        response.addCookie(cookie);
//
//        //Authorization: Bearer 인증토큰string
//        response.addHeader("Authorization", "Bearer " + token);
//
//        System.out.println("success~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        System.out.println("fail~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true); //https 통신을 진행할 경우
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}

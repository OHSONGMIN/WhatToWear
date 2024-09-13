package com.example.backend.jwt;

import com.example.backend.dto.CustomUserDetails;
import com.example.backend.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // "/api/main/reissue" 라면 다음 필터로 넘김
        String requestURI = request.getRequestURI();
        if ("/api/main/reissue".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        // 토큰이 있다면
        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);

        } catch (ExpiredJwtException e) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired - JWTFilter");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } // 토큰이 만료되면 토큰이 없을 때처럼
          // 다음 필터로 넘기지 않고 응답 코드를 발생시켜서 토큰이 만료되었다고 응답

        // 토큰이 있고, 만료되지 않았다면
        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        // accessToken이 아니면 accessToken이 아니라고 응답 메시지와 함께 상태코드를 응답
        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } // 마찬가지로 응답하고 다음 필터로 넘기면 안됨

        // 토큰 검증 완료 !!
        // 토큰 내부에서 username, role 값을 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //Member Entity 생성하여 값 set
        Member member = new Member();
        member.setEmail(username);
        member.setPassword("temppassword"); //임시 비밀번호, DB에 요청할 필요 없음
        member.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(username);
//        userEntity.setRole(role);
//        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세선에 사용자 등록(유저 세션 생성)
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

        /*

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("JWTFilter - token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료
            return;
        }

        System.out.println("authorization now - token 있음");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("JWTFilter - token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username(사실 email)과 role 획득
        String username = jwtUtil.getUsername(token); //사실 email
        String role = jwtUtil.getRole(token);

        //Member Entity 생성하여 값 set
        Member member = new Member();
        member.setEmail(username);
        member.setPassword("temppassword"); //임시 비밀번호, DB에 요청할 필요 없음
        member.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세선에 사용자 등록(유저 세션 생성)
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //받았던 request, response를 그다음 필터에게 넘김
        filterChain.doFilter(request, response);

         */
    }
}

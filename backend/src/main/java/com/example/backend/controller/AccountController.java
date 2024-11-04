package com.example.backend.controller;

import com.example.backend.dto.CustomUserDetails;
import com.example.backend.dto.MemberInfoDto;
import com.example.backend.entity.Member;
import com.example.backend.repository.MemberRepository;
import com.example.backend.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

    @GetMapping("/api/account/info")
    public ResponseEntity getInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = memberRepository.findByEmail(userDetails.getUsername());
        int memberId = member.getId();
        //String role = member.getRole();

        //MemberInfoDto memberInfo = new MemberInfoDto(memberId, role);
        MemberInfoDto memberInfo = new MemberInfoDto(memberId);

        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }

    @GetMapping("/api/account/check_admin")
    public ResponseEntity getCheckAdmin(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = memberRepository.findByEmail(userDetails.getUsername());
        String role = member.getRole();

        if (role.equals("ROLE_ADMIN")) {

            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else {

            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }

    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);

        if (claims != null) {
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

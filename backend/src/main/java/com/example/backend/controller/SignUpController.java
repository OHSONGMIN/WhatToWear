package com.example.backend.controller;

import com.example.backend.repository.MemberRepository;
import com.example.backend.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class SignUpController {

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/api/signup/dupl/{email}")
    public ResponseEntity checkDuplEmail(
            @PathVariable("email") String email
    ) {
        boolean result = memberRepository.existsByEmail(email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/api/signup")
    public ResponseEntity signUp (@RequestBody MemberDto dto,
                                  HttpServletResponse res
    ) {


        return new ResponseEntity<>(HttpStatus.OK);
    }
}

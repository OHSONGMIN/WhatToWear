package com.example.backend.controller;

import com.example.backend.repository.MemberRepository;
import com.example.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}

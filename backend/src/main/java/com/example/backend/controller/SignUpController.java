package com.example.backend.controller;

import com.example.backend.dto.MemberDto;
import com.example.backend.entity.Member;
import com.example.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
public class SignUpController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/api/main/signup/dupl/{email}")
    public ResponseEntity checkDuplEmail(
            @PathVariable("email") String email
    ) {
        boolean result = memberRepository.existsByEmail(email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/api/main/signup")
    public ResponseEntity signUp (@RequestBody MemberDto dto) {

        Member newMember = new Member();

        newMember.setEmail(dto.getEmail());
        newMember.setPassword(bCryptPasswordEncoder.encode(dto.getPassword())); //비밀번호 암호화
        newMember.setRegdate(LocalDateTime.now());
        newMember.setRole("ROLE_USER"); //ROLE_USER

        memberRepository.save(newMember);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

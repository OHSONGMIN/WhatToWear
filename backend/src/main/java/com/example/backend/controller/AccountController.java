package com.example.backend.controller;

import com.example.backend.dto.CustomUserDetails;
import com.example.backend.dto.MemberInfoDto;
import com.example.backend.entity.Member;
import com.example.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    MemberRepository memberRepository;

    @GetMapping("/api/account/info")
    public ResponseEntity getInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = memberRepository.findByEmail(userDetails.getUsername());
        int memberId = member.getId();

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
}

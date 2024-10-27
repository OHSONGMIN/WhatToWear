package com.example.backend.controller;

import com.example.backend.dto.ChangeInfoDto;
import com.example.backend.dto.CustomUserDetails;
import com.example.backend.dto.OutfitDto;
import com.example.backend.dto.WithdrawMemberDto;
import com.example.backend.entity.Member;
import com.example.backend.entity.Outfit;
import com.example.backend.repository.MemberRepository;
import com.example.backend.repository.OutfitRepository;
import com.example.backend.repository.RefreshRepository;
import com.example.backend.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
public class MyPageController {

    @Autowired
    JwtService jwtService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RefreshRepository refreshRepository;
    @Autowired
    private OutfitRepository outfitRepository;

    @GetMapping("/api/account/getEmail")
    public ResponseEntity getEmail (
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @PostMapping("/api/account/changeInfo")
    public ResponseEntity changeInfo (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChangeInfoDto dto
    ) {
        // 기존비밀번호와 동일한지
        // 동일하다면 바꾸고, 다르면 에러 던지기

        // DB에 저장된 비밀번호
        Member member = memberRepository.findByEmail(userDetails.getUsername());
        String dbPassword = member.getPassword();

        // 사용자가 입력한 기존 비밀번호와 DB에 저장된 비밀번호 비교
        if (!bCryptPasswordEncoder.matches(dto.getOriPassword(), dbPassword)) {
            // 기존 비밀번호와 일치하지 않다면 에러 던지기
            System.out.println("기존 비밀번호와 일치하지 않아용");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // 새로운 비밀번호 암호화 후 저장
        member.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        memberRepository.save(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/api/account/withdraw")
    public ResponseEntity withdrawMember (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody WithdrawMemberDto eto,
            HttpServletResponse response
    ) {
        Member member = memberRepository.findByEmail(userDetails.getUsername());
        String password = member.getPassword();

        if (!bCryptPasswordEncoder.matches(eto.getPassword(), password)) {
            // 기존 비밀번호와 일치하지 않으면 탈퇴 못함
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };

        member.setDelStatus(1);
        memberRepository.save(member);

        // 탈퇴 회원가 작성한 리뷰 삭제 처리 (delStatus = 1)
        outfitRepository.deleteByMemberId(member.getId());

        // 탈퇴 회원의 refresh token 제거
        refreshRepository.deleteAllByEmail(member.getEmail());

        // refresh token 쿠키 제거
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        //response.setStatus(HttpServletResponse.SC_OK);
//
//        System.out.println("refresh??" + cookie.getValue());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

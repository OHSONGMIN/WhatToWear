package com.example.backend.controller;

import com.example.backend.entity.Member;
import com.example.backend.entity.Outfit;
import com.example.backend.repository.AdminRepository;
import com.example.backend.repository.MemberRepository;
import com.example.backend.repository.OutfitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private OutfitRepository outfitRepository;
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/api/admin/searchMember")
    public ResponseEntity searchMember(
            @RequestParam("email") String email
            ) {

        List<Member> members = adminRepository.findByKeyword(email);

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/api/admin/memberInfo")
    public ResponseEntity getMemberInfo(
            @RequestParam("email") String email
    ) {

        Member member = adminRepository.findByEmail(email);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdraw/{email}")
    public ResponseEntity withdrawMember(
            @PathVariable("email") String email
    ) {
        Member member = adminRepository.findByEmail(email);
        member.setDelStatus(1);
        adminRepository.save(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/api/admin/getOutfits/{email}")
    public ResponseEntity getMemberOutfits(
            @PathVariable("email") String email
    ) {
        int memberId = memberRepository.findByEmail(email).getId();

        List<Outfit> outfits = outfitRepository.findAllOutfitsByMemberId(memberId);

        return new ResponseEntity<>(outfits, HttpStatus.OK);
    }

}
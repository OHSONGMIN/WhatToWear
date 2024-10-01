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

import java.time.LocalDateTime;
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

    @GetMapping("/api/admin/searchOutfit")
    public ResponseEntity getMemberOutfit(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate

    ) {
        //System.out.println(startDate + "부터요 언제까지?" + endDate);

        LocalDateTime localStartDate = LocalDateTime.of(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(5, 7)), Integer.parseInt(startDate.substring(8, 10)), 0, 0);
        LocalDateTime localEndDate = LocalDateTime.of(Integer.parseInt(endDate.substring(0, 4)), Integer.parseInt(endDate.substring(5, 7)), Integer.parseInt(endDate.substring(8, 10)), 23, 59, 59);

        //System.out.println(localStartDate + " " + localEndDate);

        List<Outfit> outfits = outfitRepository.findOutfitsRange(localStartDate, localEndDate);

        return new ResponseEntity<>(outfits, HttpStatus.OK);
    }

    @GetMapping("/api/admin/outfits")
    public ResponseEntity getAllOutfits () {

        List<Outfit> outfits = outfitRepository.findAllOutfits();

        return new ResponseEntity<>(outfits, HttpStatus.OK);
    }

}
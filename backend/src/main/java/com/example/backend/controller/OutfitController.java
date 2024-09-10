package com.example.backend.controller;

import com.example.backend.dto.CustomUserDetails;
import com.example.backend.dto.OutfitDto;
import com.example.backend.entity.Detail;
import com.example.backend.entity.Outfit;
import com.example.backend.repository.DetailRepository;
import com.example.backend.repository.MemberRepository;
import com.example.backend.repository.OutfitRepository;
import com.example.backend.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
public class OutfitController {

    @Autowired
    JwtService jwtService;

    @Autowired
    private OutfitRepository outfitRepository;
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/api/outfit/write")
    public ResponseEntity pushOutfit(
            @RequestBody OutfitDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        int memberId = memberRepository.findByEmail(userDetails.getUsername()).getId();
        Outfit newOutfit = new Outfit();

        //1. outfits DB에 먼저 담고 + 나중에 지역, 온도도 추가해야함
        newOutfit.setMemberId(memberId);
        newOutfit.setReview(dto.getReview());
        newOutfit.setRegdate(LocalDateTime.now());
        newOutfit.setRegion(dto.getAddress());
        newOutfit.setOvercoat(dto.getOvercoat());
        newOutfit.setTop(dto.getTop());
        newOutfit.setBottom(dto.getBottom());
        newOutfit.setAccessory(dto.getAccessory());

        outfitRepository.save(newOutfit);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/api/main/outfits")
    public ResponseEntity getOutfits() {

        List<Outfit> outfits = outfitRepository.findOutfits();

        return new ResponseEntity<>(outfits, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/api/delOutfit/{outfitId}")
    public ResponseEntity deleteOutfit(
            @PathVariable("outfitId") int outfitId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        int memberId = memberRepository.findByEmail(userDetails.getUsername()).getId();

        Outfit outfit = outfitRepository.findById(outfitId);

        // outfit이 존재하지 않을 경우
        if (outfit == null) {
            return new ResponseEntity<>("Outfit not found", HttpStatus.NOT_FOUND);
        }

        // outfit의 memberId와 token의 email로 불러온 memberId가 일치하는지 확인
        if (outfit.getMemberId() == memberId || memberId == 1) {
            // delStatus를 1로 업데이트
            outfit.setDelStatus(1);
            outfitRepository.save(outfit);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 권한이 없으면 403 반환 (memberId가 다름)
            return new ResponseEntity<>("리뷰 삭제 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/api/outfit/history")
    public ResponseEntity getHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        int memberId = memberRepository.findByEmail(userDetails.getUsername()).getId();

        List<Outfit> outfits = outfitRepository.findByMemberIdOrderByIdDesc(memberId);

        return new ResponseEntity<>(outfits, HttpStatus.OK);
    }
}

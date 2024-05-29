package com.example.backend.controller;

import com.example.backend.dto.OutfitDto;
import com.example.backend.entity.Detail;
import com.example.backend.entity.Outfit;
import com.example.backend.repository.DetailRepository;
import com.example.backend.repository.OutfitRepository;
import com.example.backend.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


@RestController
public class OutfitController {

    @Autowired
    JwtService jwtService;

    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private DetailRepository detailRepository;


    @PostMapping("/api/write")
    public ResponseEntity pushOutfit(
            @RequestBody OutfitDto dto,
            @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Outfit newOutfit = new Outfit();

        //1. outfits DB에 먼저 담고 + 나중에 지역, 온도도 추가해야함
        newOutfit.setMemberId(memberId);
        newOutfit.setReview(dto.getReview());
        newOutfit.setRegdate(LocalDateTime.now());
        newOutfit.setRegion("서울");
        newOutfit.setOvercoat(dto.getOvercoat());
        newOutfit.setTop(dto.getTop());
        newOutfit.setBottom(dto.getBottom());
        newOutfit.setAccessory(dto.getAccessory());

        outfitRepository.save(newOutfit);

        return new ResponseEntity<>(HttpStatus.OK);
    }
/*
    private void saveDetails(int outfitId, int[] itemIds) {
        for (Integer itemId : itemIds) {
            Detail newDetail = new Detail();
            newDetail.setOutfitId(outfitId);
            newDetail.setItemId(itemId);
            detailRepository.save(newDetail);
        }
    }
    */


    @GetMapping("/api/outfits")
    public ResponseEntity getOutfits() {

        List<Outfit> outfits = outfitRepository.findOutfits();

        return new ResponseEntity<>(outfits, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/api/outfit/{outfitId}")
    public ResponseEntity deleteOutfit(
            @PathVariable("outfitId") int outfitId,
            @CookieValue(value = "token", required = false) String token
    ) {
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new EntityNotFoundException("Outfit not found"));
        outfit.setDelStatus(1);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

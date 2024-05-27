package com.example.backend.controller;

import com.example.backend.dto.OutfitDto;
import com.example.backend.entity.Outfit;
import com.example.backend.entity.OutfitItem;
import com.example.backend.repository.OutfitRepository;
import com.example.backend.service.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@Transactional
@RestController
public class OutfitController {

    @Autowired
    JwtService jwtService;

    @Autowired
    private OutfitRepository outfitRepository;

    @PostMapping("/api/write")
    public ResponseEntity pushOutfit(
            @RequestBody OutfitDto dto,
            @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Outfit newOutfit = new Outfit();

        //1. outfits DB에 먼저 담고
        newOutfit.setMemberId(memberId);
        newOutfit.setOvercoat(dto.getOvercoat());
        newOutfit.setTop(dto.getTop());
        newOutfit.setBottom(dto.getBottom());
        newOutfit.setAccessory(dto.getAccessory());
        newOutfit.setReview(dto.getReview());
        newOutfit.setRegdate(LocalDateTime.now());

        outfitRepository.save(newOutfit);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

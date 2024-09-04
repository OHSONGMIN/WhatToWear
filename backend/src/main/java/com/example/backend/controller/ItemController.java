package com.example.backend.controller;

import com.example.backend.entity.Item;
import com.example.backend.repository.ItemRepository;
import com.example.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    JwtService jwtService;

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/api/outfit/items")
    public ResponseEntity getItems(
//            @CookieValue(value = "token", required = true) String token
    ) {
//        System.out.println("token : " + token);
//
//        if (!jwtService.isValid(token)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }

        List<Item> items = itemRepository.findAll();

        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}

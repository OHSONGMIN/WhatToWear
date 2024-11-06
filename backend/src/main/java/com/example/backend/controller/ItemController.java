package com.example.backend.controller;

import com.example.backend.entity.Item;
import com.example.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/api/outfit/items")
    public ResponseEntity getItems() {

        List<Item> items = itemRepository.findActiveItem();

        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}

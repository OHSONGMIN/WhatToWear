package com.example.backend.controller;

import com.example.backend.entity.Member;
import com.example.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/api/admin/searchMember")
    public ResponseEntity searchMember(
            @RequestParam("email") String email
            ) {

        List<Member> members = adminRepository.findByEmail(email);

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

}
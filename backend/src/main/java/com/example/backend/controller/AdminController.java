package com.example.backend.controller;

import com.example.backend.dto.SearchKeywordDto;
import com.example.backend.entity.Member;
import com.example.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/api/admin/searchMember")
    public ResponseEntity searchMember(
            @RequestBody SearchKeywordDto dto
            ) {

        System.out.println("도착??");
        List<Member> members = adminRepository.findByEmail(dto.getKeyword());

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

}
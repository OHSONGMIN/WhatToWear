package com.example.backend.service;

import com.example.backend.dto.MemberDto;
import com.example.backend.entity.Member;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JoinService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(MemberDto memberDto) {

        String email = memberDto.getEmail();
        String password = memberDto.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {

            return;
        }

        Member data = new Member();

        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");
        data.setRegdate(LocalDateTime.now()); //내가 추가함

        userRepository.save(data);
    }
}

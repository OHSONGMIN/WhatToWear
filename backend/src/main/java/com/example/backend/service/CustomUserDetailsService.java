package com.example.backend.service;

import com.example.backend.dto.CustomUserDetails;
import com.example.backend.entity.Member;
import com.example.backend.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    //DB 연결 - DB에 접근할 수 있는 MemberRepository 주입받음
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // DB에서 조회
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            // 미가입
            throw new UsernameNotFoundException("Member not fount");
        }

        if (member.getDelStatus() == 1) {
            // 탈퇴회원
            throw new IllegalStateException("Member is deactivated");
        }

        // member에 담아서 return하면 AutneticationManager가 검증
        return new CustomUserDetails(member);
    }
}

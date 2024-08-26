package com.example.backend.repository;

import com.example.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Integer> {

    Boolean existsByEmail(String email);

    //email을 받아 DB 테이블에서 회원을 조회
    Member findByEmail(String email);
}
